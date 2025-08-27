package knemognition.heartauth.orchestrator.external.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.DeviceCredentialCreateMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.PairingCreateMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalPairingService;
import knemognition.heartauth.orchestrator.external.app.utils.CryptoUtils;
import knemognition.heartauth.orchestrator.external.app.utils.PairingJwtClaimsProvider;
import knemognition.heartauth.orchestrator.external.config.OrchestratorProps;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.DeviceCredentialStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.PairingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalPairingServiceImpl implements ExternalPairingService {

    private final PairingJwtClaimsProvider claimsProvider;
    private final OrchestratorProps props;
    private final PairingStore pairingStore;
    private final DeviceCredentialStore credentialGateway;
    private final PairingCreateMapper pairingCreateMapper;
    private final DeviceCredentialCreateMapper deviceCredentialCreateMapper;
    private final ObjectMapper objectMapper;

    @Override
    public PairingInitResponse initPairing(PairingInitRequest req) {
        // 1) Verify pairing JWT from Authorization header
        var claims = claimsProvider.getClaimsOrThrow();

        Instant now = Instant.now();
        long ttlByJwt = Math.max(0, claims.exp().getEpochSecond() - now.getEpochSecond());
        long ttl = Math.min(ttlByJwt, props.getPairing().getMaxTtlSeconds());
        if (ttl <= 0) throw new IllegalArgumentException("pairing_token_expired");

        // 2) Create nonce
        byte[] nonce = new byte[props.getPairing().getNonceBytes()];
        new java.security.SecureRandom().nextBytes(nonce);
        String nonceB64 = Base64.getEncoder().encodeToString(nonce);

        long exp = now.plusSeconds(ttl).getEpochSecond();
        long created = now.getEpochSecond();

        // 3) Persist pairing state (via mapper)
        PairingState st = pairingCreateMapper.toState(
                req,
                claims.jti(),
                claims.userId(),
                nonceB64,
                exp,
                created,
                ttl
        );
        pairingStore.create(st, Duration.ofSeconds(ttl));

        // 4) Compute policy (devices remaining)
        long active = credentialGateway.countActiveByUserId(claims.userId());
        int remaining = Math.max(0, props.getMaxDevicesPerUser() - (int) active);

        // 5) Build response
        PairingInitResponse resp = new PairingInitResponse();
        resp.setJti(claims.jti());
        resp.setNonce(nonceB64);
        resp.setExp(st.getExp());

        PairingInitResponsePolicy policy = new PairingInitResponsePolicy();
        policy.setDeviceLimitRemaining(remaining);
        resp.setPolicy(policy);
        return resp;
    }

    @Override
    public PairingConfirmResponse confirmPairing(PairingConfirmRequest req) {
        // 1) Load pairing state
        UUID jti = Objects.requireNonNull(req.getJti(), "jti required");
        PairingState st = pairingStore.get(jti)
                .orElseThrow(() -> new IllegalStateException("pairing_not_found_or_expired"));

        // Replay check: if already approved, deny (reason=replayed)
        if (st.getStatus() == FlowStatus.APPROVED) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "replayed");
            throw new IllegalStateException("pairing_replayed");
        }

        // Expired?
        long nowSec = Instant.now().getEpochSecond();
        if (st.getExp() != null && st.getExp() <= nowSec) {
            pairingStore.changeStatus(jti, FlowStatus.EXPIRED, null);
            throw new IllegalStateException("pairing_expired");
        }

        // Device must match init
        if (!Objects.equals(st.getDeviceId(), req.getDeviceId())) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "device_mismatch");
            throw new IllegalArgumentException("device_mismatch");
        }

        // 2) Verify PoP signature
        if (!"ES256".equals(req.getAlg())) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "unsupported_alg");
            throw new IllegalArgumentException("unsupported_alg");
        }
        try {
            PublicKey pub = CryptoUtils.parseECP256PublicKeyFromPEM(st.getPublicKeyPem());
            byte[] nonce = Base64.getDecoder().decode(st.getNonceB64());
            byte[] msg = CryptoUtils.concat(
                    nonce,
                    CryptoUtils.ascii(jti.toString()),
                    CryptoUtils.ascii(req.getDeviceId())
            );
            byte[] sig = Base64.getUrlDecoder().decode(req.getSignature());
            if (!CryptoUtils.verifyES256(pub, msg, sig)) {
                pairingStore.changeStatus(jti, FlowStatus.DENIED, "bad_signature");
                throw new SecurityException("bad_signature");
            }
        } catch (SecurityException se) {
            throw se;
        } catch (Exception e) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "signature_verification_failed");
            throw new IllegalArgumentException("signature_verification_failed", e);
        }

        // 3) Policy checks
        long activeByUser = credentialGateway.countActiveByUserId(st.getUserId());
        if (activeByUser >= props.getMaxDevicesPerUser()) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "policy_device_limit");
            throw new SecurityException("policy_device_limit");
        }

        var existingActiveForDevice = credentialGateway.findActiveByDeviceId(st.getDeviceId());
        if (existingActiveForDevice.isPresent()
                && !existingActiveForDevice.get().getUserId().equals(st.getUserId())) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "device_already_linked_elsewhere");
            throw new SecurityException("device_already_linked_elsewhere");
        }

        // 4) Persist credential (Postgres) via mapper
        knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential saved;
        try {
            var domainCred = deviceCredentialCreateMapper.fromPairingState(st, objectMapper);
            saved = credentialGateway.create(domainCred);
        } catch (DataIntegrityViolationException e) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "db_constraint");
            throw e;
        }

        // 5) Success: mark APPROVED
        pairingStore.changeStatus(jti, FlowStatus.APPROVED, null);

        // 6) Build response (generated models)
        PairingConfirmResponse out = new PairingConfirmResponse();
        out.setStatus(FlowStatus.APPROVED);

        DeviceCredential cred = new DeviceCredential();
        cred.setDeviceId(saved.getDeviceId());
        cred.setDisplayName(saved.getDisplayName());
        cred.setPublicKeyPem(saved.getPublicKeyPem());
        cred.setFcmToken(saved.getFcmToken());
        cred.setCreatedAt(saved.getCreatedAt() != null ? saved.getCreatedAt().getEpochSecond() : nowSec);

        if (st.getAttestationType() != null) {
            Attestation att = new Attestation();
            att.setType(Attestation.TypeEnum.fromValue(st.getAttestationType()));
            att.setVerdict(st.getAttestationVerdict());
            cred.setAttestation(att);
        }

        out.setCredential(cred);
        return out;
    }
}
