package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.mapper.InitPairingMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.external.app.ports.out.EnrichDeviceDataStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitPairingServiceImpl implements InitPairingService {

    private final InitPairingMapper initPairingMapper;
    private final EnrichDeviceDataStore enrichDeviceDataStore;
    private final StatusStore<PairingState> pairingStateStatusStore;

    @Override
    public PairingInitResponse init(PairingInitRequest req, QrClaims claims) {

        String nonceB64 = createNonce();
        UUID id = claims.getJti();

        FlowStatusDescription status = pairingStateStatusStore.getStatus(id).orElseThrow(() -> new StatusServiceException("Status not found"));
        if (status.getStatus() != FlowStatus.CREATED) {
            throw new StatusServiceException("Pairing already initialized");
        }

        EnrichDeviceData to = initPairingMapper.toEnrichDeviceData(req, nonceB64, id);
        enrichDeviceDataStore.enrich(to);
        log.info("Device data stored");
        pairingStateStatusStore.setStatusOrThrow(StatusChange.builder().id(id).status(FlowStatus.PENDING).build());
        return new PairingInitResponse().nonce(nonceB64);
    }

    private String createNonce() {
        byte[] nonce = new byte[32];
        new SecureRandom().nextBytes(nonce);
        return Base64.getEncoder().encodeToString(nonce);
    }
}
