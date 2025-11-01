package knemognition.heartauth.orchestrator.challenges.app.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import knemognition.heartauth.orchestrator.challenges.api.ChallengeFailedException;
import knemognition.heartauth.orchestrator.challenges.api.CompleteChallengeWithPredictionPayloadCmd;
import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.api.NoChallengeException;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengesMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.EcgTestTokenClaims;
import knemognition.heartauth.orchestrator.challenges.domain.FlowStatusReason;
import knemognition.heartauth.orchestrator.challenges.domain.StatusChange;
import knemognition.heartauth.orchestrator.ecg.api.EcgModule;
import knemognition.heartauth.orchestrator.ecg.api.GetRefDataCmd;
import knemognition.heartauth.orchestrator.modelapi.api.EcgSendPredictCmd;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiSendApi;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.SecurityModule;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteChallengeHandler {

    private final EcgModule ecgModule;
    private final UserModule userModule;
    private final SecurityModule securityModule;
    private final ModelApiSendApi modelApiSendApi;

    private final ChallengesMapper challengesMapper;
    private final ChallengeStore internalChallengeStore;
    private final Duration POLL_INTERVAL = Duration.ofMillis(250);
    private final Duration TIMEOUT = Duration.ofSeconds(20);


    public boolean handle(CompleteChallengeWithPredictionPayloadCmd cmd) {
        try {
            completeChallengeInternal(cmd);

            var finalStatus = awaitApprovalOrTimeout(cmd.getChallengeId(), TIMEOUT).block();
            return finalStatus == FlowStatus.APPROVED;

        } catch (NoChallengeException | ChallengeFailedException e) {
            log.info("[CHALLENGE] Challenge {} failed fast: {}", cmd.getChallengeId(), e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("[CHALLENGE] Challenge {} unexpected error", cmd.getChallengeId(), e);
            return false;
        }
    }


    @SneakyThrows
    private void completeChallengeInternal(CompleteChallengeWithPredictionPayloadCmd cmd) {

        var state = internalChallengeStore.getFlow(cmd.getChallengeId())
                .orElseThrow(() -> new NoChallengeException("challenge_not_found_or_expired"));

        var validateNonce = challengesMapper.toCmd(cmd, state);
        securityModule.validateNonce(validateNonce);
        log.info("[CHALLENGE] Nonce has been successfully validated");

        if (state.getStatus() == FlowStatus.APPROVED) {
            throw new NoChallengeException("challenge_replayed");
        }
        DecryptJweCmd<EcgTestTokenClaims> toDecryptJwe = DecryptJweCmd.<EcgTestTokenClaims>builder()
                .jwe(cmd.getDataToken())
                .recipientPrivateKey(state.getEphemeralPrivateKey())
                .senderPublicKey(validateNonce.getPub())
                .targetType(new TypeReference<>() {
                })
                .build();

        EcgTestTokenClaims ecgTestTokenClaims = securityModule.decryptJwe(toDecryptJwe);

        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(cmd.getChallengeId());
        var user = userModule.getUser(challengesMapper.toCmd(state))
                .orElseThrow();
        var refData = ecgModule.getUserReferenceData(GetRefDataCmd.builder()
                .userId(user.getId())
                .build());
        modelApiSendApi.handle(EcgSendPredictCmd.builder()
                .correlationId(state.getModelApiTryId())
                .testEcg(ecgTestTokenClaims.getTestEcg())
                .refEcg(refData.getRefEcg())
                .build());
        log.info("[CHALLENGE] Posted Kafka message for ECG prediction for challengeId {}", cmd.getChallengeId());
        internalChallengeStore.setStatus(statusChangeBuilder.status(FlowStatus.PENDING)
                .reason(FlowStatusReason.FLOW_WAITING_FOR_MODEL)
                .build());
        log.info("[CHALLENGE] Challenge {} status set to PENDING", cmd.getChallengeId());
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
        return internalChallengeStore.getFlow(id)
                .map(ChallengeState::getStatus)
                .orElse(FlowStatus.NOT_FOUND);
    }
}
