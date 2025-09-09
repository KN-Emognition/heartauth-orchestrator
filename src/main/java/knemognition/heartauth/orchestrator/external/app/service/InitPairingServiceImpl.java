package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.mapper.InitPairingMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.PemMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.InitPairingService;
import knemognition.heartauth.orchestrator.external.config.pairing.ExternalPairingProperties;
import knemognition.heartauth.orchestrator.external.model.*;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.external.app.ports.out.EnrichDeviceDataStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

import static knemognition.heartauth.orchestrator.shared.utils.NonceUtils.createNonce;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ExternalPairingProperties.class)
public class InitPairingServiceImpl implements InitPairingService {

    private final ExternalPairingProperties externalPairingProperties;
    private final InitPairingMapper initPairingMapper;
    private final SecureRandom secureRandom;
    private final EnrichDeviceDataStore enrichDeviceDataStore;
    private final StatusStore<PairingState> pairingStateStatusStore;
    private final PemMapper pemMapper;

    @Override
    public PairingInitResponse init(PairingInitRequest req, QrClaims claims) {

        pemMapper.map(req.getPublicKeyPem());

        String nonceB64 = createNonce(secureRandom, externalPairingProperties.getNonceLength());
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
}
