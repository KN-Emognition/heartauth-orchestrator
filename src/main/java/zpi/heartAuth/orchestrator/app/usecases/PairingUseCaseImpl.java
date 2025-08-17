//package zpi.heartAuth.orchestrator.app.usecases;
//
//// app/usecases/PairingUseCaseImpl.java
//
//import lombok.RequiredArgsConstructor;
//import zpi.heartAuth.orchestrator.app.api.PairingUseCase;
//import zpi.heartAuth.orchestrator.app.models.*;
//import zpi.heartAuth.orchestrator.gateways.crypto.CryptoVerifier;
//import zpi.heartAuth.orchestrator.gateways.repository.*;
//import zpi.heartAuth.orchestrator.gateways.security.*;
//import zpi.heartAuth.orchestrator.gateways.system.*;
//
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.time.Instant;
//import java.util.*;
//
//@RequiredArgsConstructor
//public class PairingUseCaseImpl implements PairingUseCase {
//
//    private final PairingTokenValidator tokenValidator;
//    private final PairingStateStore pairingStore;
//    private final DeviceCredentialRepository credentials;
//    private final CryptoVerifier crypto;
//    private final DPoPVerifier dpop;
//    private final ClockProvider clock;
//    private final IdGenerator ids;
//    private final long ttlSeconds;
//    private final int deviceLimit;
//
//    @Override
//    public InitResult init(String pairingBearer, String deviceId, String displayName,
//                           String publicKeyPem, String fcmToken, String attType, String attVerdict) {
//
//        var claims = tokenValidator.validate(pairingBearer);
//        var now = clock.now();
//
//        int count = credentials.countByUser(claims.userId());
//        int remaining = Math.max(0, deviceLimit - count);
//        if (remaining <= 0) throw ServiceException.unprocessable("device_limit", "Device limit reached");
//
//        byte[] nonce = ids.randomBytes(32);
//        Instant exp = now.plusSeconds(ttlSeconds);
//
//        var state = PairingState.builder()
//                .jti(claims.jti()).userId(claims.userId())
//                .deviceId(deviceId).displayName(displayName)
//                .publicKeyPem(publicKeyPem).fcmToken(fcmToken)
//                .attestationType(attType == null ? "none" : attType)
//                .attestationVerdict(attVerdict)
//                .nonce(nonce).expiresAt(exp).status(PairingStatus.PENDING)
//                .build();
//
//        boolean created = pairingStore.createIfAbsent(state);
//        if (!created) throw ServiceException.conflict("jti_conflict", "Pairing already started for jti");
//
//        return new InitResult(claims.jti(), Base64.getEncoder().encodeToString(nonce), exp, remaining);
//    }
//
//    @Override
//    public ConfirmResult confirm(UUID jti, String deviceId, String signatureB64Url,
//                                 String alg, String dpopHeader, URI httpUri) {
//        var state = pairingStore.get(jti)
//                .orElseThrow(() -> ServiceException.gone("expired", "Pairing expired or not found"));
//
//        if (clock.now().isAfter(state.getExpiresAt()))
//            throw ServiceException.gone("expired", "Pairing expired");
//
//        if (state.getStatus() == PairingStatus.LINKED)
//            throw ServiceException.conflict("replayed", "Already linked");
//
//        if (!"ES256".equals(alg))
//            throw ServiceException.badRequest("alg", "Only ES256 supported");
//
//        if (!Objects.equals(deviceId, state.getDeviceId()))
//            throw ServiceException.unprocessable("device_mismatch", "deviceId mismatch");
//
//        if (dpopHeader != null && !dpopHeader.isBlank())
//            dpop.verify(dpopHeader, "POST", httpUri, state.getPublicKeyPem());
//
//        byte[] sig = Base64.getUrlDecoder().decode(signatureB64Url);
//        byte[] raw = crypto.concat(state.getNonce(),
//                jti.toString().getBytes(StandardCharsets.UTF_8),
//                deviceId.getBytes(StandardCharsets.UTF_8));
//
//        if (!crypto.verifyEs256Raw(state.getPublicKeyPem(), raw, sig))
//            throw ServiceException.unprocessable("bad_signature", "Signature verification failed");
//
//        var credential = DeviceCredential.builder()
//                .userId(state.getUserId()).deviceId(state.getDeviceId())
//                .displayName(state.getDisplayName()).publicKeyPem(state.getPublicKeyPem())
//                .fcmToken(state.getFcmToken()).attestationType(state.getAttestationType())
//                .attestationVerdict(state.getAttestationVerdict()).createdAt(clock.now())
//                .build();
//
//        credentials.upsert(credential);
//        state.setStatus(PairingStatus.LINKED);
//        pairingStore.save(state);
//
//        return new ConfirmResult(credential);
//    }
//
//    @Override
//    public StatusResult status(UUID jti) {
//        var s = pairingStore.get(jti)
//                .orElseThrow(() -> ServiceException.notFound("not_found", "Unknown jti"));
//        if (clock.now().isAfter(s.getExpiresAt()) && s.getStatus() == PairingStatus.PENDING)
//            return new StatusResult("expired", null);
//
//        return switch (s.getStatus()) {
//            case PENDING -> new StatusResult("pending", null);
//            case LINKED -> new StatusResult("linked", null);
//            case EXPIRED -> new StatusResult("expired", null);
//            case REPLAYED -> new StatusResult("replayed", "replayed");
//            case DENIED -> new StatusResult("denied", "policy");
//        };
//    }
//}
//
