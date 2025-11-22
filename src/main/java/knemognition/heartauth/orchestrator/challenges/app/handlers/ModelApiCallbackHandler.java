package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.FlowStatusReason;
import knemognition.heartauth.orchestrator.challenges.domain.StatusChange;
import knemognition.heartauth.orchestrator.modelapi.api.CompleteChallengeWithPredictionResultCmd;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiCallbackApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelApiCallbackHandler implements ModelApiCallbackApi {

    private final ChallengeStore internalChallengeStore;

    @Override
    public void handle(CompleteChallengeWithPredictionResultCmd cmd) {
        ChallengeState state = internalChallengeStore.getChallengeStateByCorrelationId(cmd.getCorrelationId());
        if (!(state.getStatus() == FlowStatus.PENDING)) {
            log.info("[MODELAPI][CHALLENGE] Prediction received for non-pending challenge {}, ignoring", state.getId());
            return;
        }
        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder()
                .id(state.getId());
        if (cmd.getError() != null) {
            log.info("[MODELAPI][CHALLENGE] Prediction error for challenge {}: {}, marking as DENIED", state.getId(),
                    cmd.getError());
            internalChallengeStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED)
                    .reason(FlowStatusReason.FLOW_DENIED_DUE_TO_MODEL_API_ERROR)
                    .build());
        } else if (cmd.getPrediction() == true) {
            log.info("[MODELAPI][CHALLENGE] Prediction approved for challenge {} : score: {}", state.getId(),
                    cmd.getScore());
            internalChallengeStore.setStatus(statusChangeBuilder.status(FlowStatus.APPROVED)
                    .reason(FlowStatusReason.FLOW_COMPLETED_SUCCESSFULLY_WITH_AUTHENTICATION)
                    .build());
        } else {
            log.info("[MODELAPI][CHALLENGE] Prediction rejected for challenge {} : score: {}", state.getId(),
                    cmd.getScore());
            internalChallengeStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED)
                    .reason(FlowStatusReason.FLOW_DENIED_WITH_AUTHENTICATION_FAILURE)
                    .build());
        }
    }
}
