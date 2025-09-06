package knemognition.heartauth.orchestrator.external.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.mapper.DeviceCredentialCreateMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.external.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.external.app.ports.out.CreateDeviceCredentialStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompletePairingServiceImpl implements CompletePairingService {

    private final GetFlowStore<PairingState> pairingStateGetFlowStore;
    private final StatusStore<PairingState> pairingStateStatusStore;
    private final CreateDeviceCredentialStore deviceCredentialStore;
    private final DeviceCredentialCreateMapper deviceCredentialCreateMapper;
    private final ObjectMapper objectMapper;

    @Override
    public StatusResponse complete(PairingConfirmRequest req, QrClaims claims) {
        UUID jti = claims.getJti();

        PairingState pairingState = pairingStateGetFlowStore.getFlow(jti)
                .orElseThrow(() -> new IllegalStateException("pairing_not_found_or_expired"));

        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder().id(jti);
        if (pairingState.getStatus() == FlowStatus.APPROVED) {
            pairingStateStatusStore.setStatus(
                    statusChangeBuilder
                            .status(FlowStatus.DENIED)
                            .build());
            throw new IllegalStateException("pairing_replayed");
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

        pairingStateStatusStore.setStatus(statusChangeBuilder
                .status(FlowStatus.APPROVED)
                .build());
        log.info("Changed cache pairing status to Approved.");


        return new StatusResponse(knemognition.heartauth.orchestrator.external.model.FlowStatus.APPROVED);
    }
}
