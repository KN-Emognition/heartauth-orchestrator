package knemognition.heartauth.orchestrator.external.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.DeviceCredentialCreateMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.PairingConfirmMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.DeviceCredentialStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.PairingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompletePairingServiceImpl implements CompletePairingService {

    private final PairingStore pairingStore;
    private final JwtServiceImpl jwtService;
    private final DeviceCredentialStore deviceCredentialStore;
    private final DeviceCredentialCreateMapper deviceCredentialCreateMapper;
    private final ObjectMapper objectMapper;
    private final PairingConfirmMapper pairingConfirmMapper;

    @Override
    public PairingConfirmResponse complete(PairingConfirmRequest req) {
        UUID jti = jwtService.process().jti();

        PairingState pairingState = pairingStore.get(jti)
                .orElseThrow(() -> new IllegalStateException("pairing_not_found_or_expired"));

        if (!Objects.equals(pairingState.getDeviceId(), req.getDeviceId())) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "device_mismatch");
            throw new IllegalArgumentException("device_mismatch");
        }

        if (pairingState.getStatus() == FlowStatus.APPROVED) {
            pairingStore.changeStatus(jti, FlowStatus.DENIED, "replayed");
            throw new IllegalStateException("pairing_replayed");
        }

        if (pairingState.getExp() <= Instant.now().getEpochSecond()) {
            pairingStore.changeStatus(jti, FlowStatus.EXPIRED, null);
            throw new IllegalStateException("pairing_expired");
        }

//        if (!"ES256".equals(req.getAlg())) {
//            pairingStore.changeStatus(jti, FlowStatus.DENIED, "unsupported_alg");
//            throw new IllegalArgumentException("unsupported_alg");
//        }
//        try {
//            PublicKey pub = CryptoUtils.parseECP256PublicKeyFromPEM(pairingState.getPublicKeyPem());
//            byte[] nonce = Base64.getDecoder().decode(pairingState.getNonceB64());
//            byte[] msg = CryptoUtils.concat(
//                    nonce,
//                    CryptoUtils.ascii(jti.toString()),
//                    CryptoUtils.ascii(req.getDeviceId())
//            );
//            byte[] sig = Base64.getUrlDecoder().decode(req.getSignature());
//            if (!CryptoUtils.verifyES256(pub, msg, sig)) {
//                pairingStore.changeStatus(jti, FlowStatus.DENIED, "bad_signature");
//                throw new SecurityException("bad_signature");
//            }
//        } catch (SecurityException se) {
//            throw se;
//        } catch (Exception e) {
//            pairingStore.changeStatus(jti, FlowStatus.DENIED, "signature_verification_failed");
//            throw new IllegalArgumentException("signature_verification_failed", e);
//        }


        DeviceCredential deviceCredential = deviceCredentialCreateMapper.fromPairingState(pairingState, objectMapper);
        deviceCredentialStore.create(deviceCredential);
        log.info("Saved device credential");

        pairingStore.changeStatus(jti, FlowStatus.APPROVED, null);
        log.info("Changed cache pairing status to Approved.");


        return pairingConfirmMapper.approved(deviceCredential, pairingState);
    }
}
