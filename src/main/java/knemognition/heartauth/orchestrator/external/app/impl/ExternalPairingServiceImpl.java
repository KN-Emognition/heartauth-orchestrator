package knemognition.heartauth.orchestrator.external.app.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.DecryptJwe;
import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.mapper.EcgTokenMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.ExternalPairingMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalPairingService;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalValidationService;
import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalMainStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalPairingStore;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoPairingException;
import knemognition.heartauth.orchestrator.external.config.pairing.ExternalPairingProperties;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.InitPairingResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.NonceService;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.interfaces.ECPrivateKey;
import java.util.UUID;

/**
 *  {@inheritDoc}
 */
@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ExternalPairingProperties.class)
public class ExternalPairingServiceImpl implements ExternalPairingService {

    // config
    private final ECPrivateKey pairingPrivateKey;
    private final ExternalPairingProperties externalPairingProperties;
    // in
    private final ExternalValidationService externalValidationService;
    private final NonceService nonceService;
    // mapper
    private final ExternalPairingMapper externalPairingMapper;
    private final EcgTokenMapper ecgTokenMapper;
    // out
    private final ExternalMainStore externalMainStore;
    private final GetFlowStore<PairingState> pairingStateGetFlowStore;
    private final ExternalPairingStore externalPairingStore;

    /**
     *  {@inheritDoc}
     */
    @Override
    public InitPairingResponseDto initPairing(InitPairingRequestDto req, QrCodeClaims qrCodeClaims) {

        externalValidationService.validatePublicKeyPem(req.getPublicKey());

        String nonceB64 = nonceService.createNonce(externalPairingProperties.getNonceLength());
        UUID id = qrCodeClaims.getJti();

        PairingState state = pairingStateGetFlowStore.getFlow(id)
                .orElseThrow(() -> new StatusServiceException("pairing_not_found_or_expired"));
        log.info("Fetched pairing state");
        if (state.getStatus() != FlowStatus.CREATED) {
            throw new StatusServiceException("Pairing already initialized");
        }
        if (!qrCodeClaims.getTenantId()
                .equals(state.getTenantId())) {
            throw new NoPairingException("No pairing for given tenant");
        }
        EnrichDeviceData to = externalPairingMapper.toEnrichDeviceData(req, nonceB64, id);
        externalPairingStore.enrichWithDeviceData(to);
        log.info("Device data stored");
        externalPairingStore.setStatusOrThrow(StatusChange.builder()
                .id(id)
                .status(FlowStatus.PENDING)
                .build());
        return InitPairingResponseDto.builder()
                .nonce(nonceB64)
                .expiresAt(state.getExp())
                .build();
    }

    /**
     *  {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void completePairing(CompletePairingRequestDto req, QrCodeClaims qrCodeClaims) {
        UUID jti = qrCodeClaims.getJti();

        PairingState state = pairingStateGetFlowStore.getFlow(jti)
                .orElseThrow(() -> new NoPairingException("pairing_not_found_or_expired"));


        ValidateNonce validateNonce = externalPairingMapper.toValidateNonce(req, state);
        externalValidationService.validateNonce(validateNonce);
        log.info("Nonce has been successfully validated");


        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(jti);
        if (!qrCodeClaims.getTenantId()
                .equals(state.getTenantId())) {
            throw new NoPairingException("No pairing for given tenant");
        }
        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoPairingException("pairing_replayed");
        }
        if (state.getStatus() != FlowStatus.PENDING) {
            throw new StatusServiceException("Pairing status is not in pending");
        }

        DecryptJwe toDecryptJwe = DecryptJwe.builder()
                .jwe(req.getDataToken())
                .recipientPrivateKey(pairingPrivateKey)
                .senderPublicKey(validateNonce.getPub())
                .build();
        JWTClaimsSet dataToken = externalValidationService.decryptAndVerifyJwe(toDecryptJwe);
        log.info("JWT has been successfully verified");
        EcgRefTokenClaims ecgRefToken = ecgTokenMapper.ecgRefFromClaimsAndState(dataToken);


        externalMainStore.savePairingArtifacts(
                externalPairingMapper.toEcgRefData(ecgRefToken),
                externalPairingMapper.toDevice(state),
                externalPairingMapper.toIdentifiableUser(state)
        );
        log.info("Saved artifacts to main store");

        externalPairingStore.setStatusOrThrow(statusChangeBuilder.status(FlowStatus.APPROVED)
                .build());
        log.info("Changed cache pairing status to Approved.");
    }
}
