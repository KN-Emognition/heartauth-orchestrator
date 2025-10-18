package knemognition.heartauth.orchestrator.external.app.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.DecryptJwe;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;
import knemognition.heartauth.orchestrator.external.app.mapper.EcgTokenMapper;
import knemognition.heartauth.orchestrator.external.app.mapper.ExternalChallengeMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalValidationService;
import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalChallengeStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalMainStore;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiKafka;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiRest;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.ChallengeFailedException;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoChallengeException;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 *  {@inheritDoc}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalChallengeServiceImpl implements ExternalChallengeService {

    // in
    private final ExternalValidationService externalValidationService;
    // mapper
    private final EcgTokenMapper ecgTokenMapper;
    private final ExternalChallengeMapper externalChallengeMapper;
    // out
    private final ModelApiRest modelApi;
    private final ModelApiKafka modelApiKafka;
    private final ExternalMainStore externalMainStore;
    private final ExternalChallengeStore challengeStateStatusStore;
    private final GetFlowStore<ChallengeState> challengeStateGetFlowStore;

    /**
     *  {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void completeChallenge(UUID challengeId, CompleteChallengeRequestDto req) {

        ChallengeState state = challengeStateGetFlowStore.getFlow(challengeId)
                .orElseThrow(() -> new NoChallengeException("challenge_not_found_or_expired"));

        ValidateNonce validateNonce = externalChallengeMapper.toValidateNonce(req, state);
        externalValidationService.validateNonce(validateNonce);
        log.info("Nonce has been successfully validated");

        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoChallengeException("challenge_replayed");
        }

        DecryptJwe toDecryptJwe = externalChallengeMapper.toDecryptJwe(req.getDataToken(),
                state.getEphemeralPrivateKey(), validateNonce.getPub());
        JWTClaimsSet claims = externalValidationService.decryptAndVerifyJwe(toDecryptJwe);
        log.info("JWT has been successfully verified");

        EcgTestTokenClaims ecgTestTokenClaims = ecgTokenMapper.ecgTestFromClaims(claims);
        Optional<EcgRefData> refData = externalMainStore.findRefData(externalChallengeMapper.toIdentifiableUser(state));
        if (refData.isEmpty()) {
            challengeStateStatusStore.setStatus(StatusChange.builder()
                    .id(challengeId)
                    .status(FlowStatus.DENIED)
                    .reason(FlowStatusReason.FLOW_DENIED_WITHOUT_AUTHENTICATION)
                    .build());
            log.info("Reference ECG data not found for user {}", state.getUserId());

            throw new ChallengeFailedException("No reference ECG data found");
        }

        log.info("Found reference data for user {}", state.getUserId());
        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(challengeId);

        EcgPrediction request = EcgPrediction.builder()
                .testEcg(ecgTestTokenClaims.getTestEcg())
                .refEcg(refData.get()
                        .getRefEcg())
                .build();

        boolean approved = modelApi.predict(request);
        modelApiKafka.predict(request);
        log.info("Called model for prediction.");
        if (!approved) {
            challengeStateStatusStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED)
                    .reason(FlowStatusReason.FLOW_DENIED_WITH_AUTHENTICATION_FAILURE)
                    .build());
            throw new ChallengeFailedException("ECG Validation failed");
        }
        challengeStateStatusStore.setStatus(statusChangeBuilder.status(FlowStatus.APPROVED)
                .reason(FlowStatusReason.FLOW_COMPLETED_SUCCESSFULLY_WITH_AUTHENTICATION)
                .build());
    }
}
