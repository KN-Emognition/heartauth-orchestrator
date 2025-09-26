package knemognition.heartauth.orchestrator.external.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.mapper.ConfirmPairingMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.DeviceCredentialCreateMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompletePairingService;
import knemognition.heartauth.orchestrator.external.app.ports.in.ValidateNonceService;
import knemognition.heartauth.orchestrator.external.app.ports.out.CreateDeviceCredentialStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoPairingException;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;
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
    private final ConfirmPairingMapper confirmPairingMapper;
    private final ValidateNonceService validateNonceService;
    private final ObjectMapper objectMapper;

    @Override
    public void complete(PairingConfirmRequest req, QrClaims claims) {
        UUID jti = claims.getJti();

        PairingState pairingState = pairingStateGetFlowStore.getFlow(jti)
                .orElseThrow(() -> new NoPairingException("pairing_not_found_or_expired"));

        ValidateNonce validateNonce = confirmPairingMapper.toValidateNonce(req, pairingState);
        validateNonceService.validate(validateNonce);
        log.info("Nonce has been successfully validated");

        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder().id(jti);

        if (pairingState.getStatus() == FlowStatus.APPROVED) {
            throw new NoPairingException("pairing_replayed");
        }
        if (pairingState.getStatus() != FlowStatus.PENDING) {
            throw new StatusServiceException("Pairing status is not in pending");
        }

        DeviceCredential deviceCredential = deviceCredentialCreateMapper.fromPairingState(pairingState, objectMapper);
        deviceCredentialStore.create(deviceCredential);
        log.info("Saved device credential");

        pairingStateStatusStore.setStatusOrThrow(statusChangeBuilder
                .status(FlowStatus.APPROVED)
                .build());
        log.info("Changed cache pairing status to Approved.");
    }
}
