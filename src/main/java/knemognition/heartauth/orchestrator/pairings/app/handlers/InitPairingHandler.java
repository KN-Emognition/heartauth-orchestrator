package knemognition.heartauth.orchestrator.pairings.app.handlers;

import knemognition.heartauth.orchestrator.pairings.api.InitPairingCmd;
import knemognition.heartauth.orchestrator.pairings.api.InitPairingRead;
import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.pairings.app.mappers.PairingsMapper;
import knemognition.heartauth.orchestrator.pairings.app.ports.PairingStore;
import knemognition.heartauth.orchestrator.pairings.config.PairingProperties;
import knemognition.heartauth.orchestrator.pairings.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.pairings.domain.StatusChange;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitPairingHandler {
    private final SecurityModule securityModule;
    private final PairingProperties pairingProperties;
    private final PairingStore pairingStore;
    private final PairingsMapper pairingsMapper;

    public InitPairingRead handle(InitPairingCmd cmd) {
        var qrCodeClaims = securityModule.getQrClaims();
        securityModule.validatePublicKeyPem(cmd.getPublicKey());

        String nonceB64 = securityModule.createNonce(pairingProperties.getNonceLength());
        UUID id = qrCodeClaims.getJti();

        PairingState state = pairingStore.getFlow(id)
                .orElseThrow(() -> new NoPairingException("pairing_not_found_or_expired"));
        log.info("Fetched pairing state");
        if (state.getStatus() != FlowStatus.CREATED) {
            throw new NoPairingException("Pairing already initialized");
        }
        if (!qrCodeClaims.getTenantId()
                .equals(state.getTenantId())) {
            throw new NoPairingException("No pairing for given tenant");
        }
        EnrichDeviceData to = pairingsMapper.toEnrichDeviceData(cmd, nonceB64, id);
        pairingStore.enrichWithDeviceData(to);
        log.info("Device data stored");
        pairingStore.setStatusOrThrow(StatusChange.builder()
                .id(id)
                .status(FlowStatus.PENDING)
                .reason(FlowStatusReason.FLOW_INITIALIZED_ON_MOBILE_DEVICE)
                .build());
        return InitPairingRead.builder()
                .nonce(nonceB64)
                .expiresAt(state.getExp())
                .build();
    }
}
