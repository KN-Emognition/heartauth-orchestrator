package knemognition.heartauth.orchestrator.external.app.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import knemognition.heartauth.orchestrator.ecg.api.EcgEvaluateCmd;
import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.external.app.mapper.ExternalChallengeMapper;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalChallengeStore;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.ChallengeFailedException;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoChallengeException;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgTestTokenClaims;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import knemognition.heartauth.orchestrator.users.api.UserModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 *  {@inheritDoc}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalChallengeServiceImpl implements ExternalChallengeService {

    private final SecurityModule securityModule;
    private final EcgModule ecgModule;
    private final UserModule userModule;
    // mapper
    private final ExternalChallengeMapper externalChallengeMapper;
    // out
    private final ExternalChallengeStore challengeStateStatusStore;
    private final GetFlowStore<ChallengeState> challengeStateGetFlowStore;

    private final Duration POLL_INTERVAL = Duration.ofMillis(250);

    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean completeChallengeAndAwait(UUID challengeId, CompleteChallengeRequestDto req, Duration timeout) {
        try {
            completeChallengeInternal(challengeId, req);

            var finalStatus = awaitApprovalOrTimeout(challengeId, timeout).block();
            return finalStatus == FlowStatus.APPROVED;

        } catch (NoChallengeException | ChallengeFailedException e) {
            log.info("Challenge {} failed fast: {}", challengeId, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Challenge {} unexpected error", challengeId, e);
            return false;
        }
    }


    @SneakyThrows
    private void completeChallengeInternal(UUID challengeId, CompleteChallengeRequestDto req) {

        var state = challengeStateGetFlowStore.getFlow(challengeId)
                .orElseThrow(() -> new NoChallengeException("challenge_not_found_or_expired"));

        var validateNonce = externalChallengeMapper.toValidateNonceCmd(req, state);
        securityModule.validateNonce(validateNonce);
        log.info("Nonce has been successfully validated");

        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoChallengeException("challenge_replayed");
        }
        DecryptJweCmd<EcgTestTokenClaims> toDecryptJwe = DecryptJweCmd.<EcgTestTokenClaims>builder()
                .jwe(req.getDataToken())
                .recipientPrivateKey(state.getEphemeralPrivateKey())
                .senderPublicKey(validateNonce.getPub())
                .targetType(new TypeReference<>() {
                })
                .build();

        EcgTestTokenClaims ecgTestTokenClaims = securityModule.decryptJwe(toDecryptJwe);

        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(challengeId);
        var user = userModule.getUser(externalChallengeMapper.toIdentifiableUserCmd(state))
                .orElseThrow();
        ecgModule.sendEcgEvaluateRequest(EcgEvaluateCmd.builder()
                .correlationId(state.getModelApiTryId())
                .userId(user.getId())
                .testEcg(ecgTestTokenClaims.getTestEcg())
                .build()

        );
        log.info("Posted Kafka message for ECG prediction for challengeId {}", challengeId);
        challengeStateStatusStore.setStatus(statusChangeBuilder.status(FlowStatus.PENDING)
                .reason(FlowStatusReason.FLOW_WAITING_FOR_MODEL)
                .build());
        log.info("Challenge {} status set to PENDING", challengeId);
    }

    private Mono<FlowStatus> awaitApprovalOrTimeout(UUID challengeId, Duration timeout) {
        Mono<FlowStatus> readOnce = Mono.fromCallable(() -> getChallengeStatus(challengeId))
                .subscribeOn(Schedulers.boundedElastic());

        return Flux.interval(POLL_INTERVAL)
                .startWith(0L)
                .flatMap(__ -> readOnce)
                .takeUntil(s -> s == FlowStatus.APPROVED || s == FlowStatus.DENIED || s == FlowStatus.NOT_FOUND)
                .timeout(timeout)
                .onErrorResume(TimeoutException.class, __ -> Mono.empty())
                .last()
                .switchIfEmpty(readOnce);
    }

    private FlowStatus getChallengeStatus(UUID id) {
        return challengeStateGetFlowStore.getFlow(id)
                .map(ChallengeState::getStatus)
                .orElse(FlowStatus.NOT_FOUND);
    }
}
