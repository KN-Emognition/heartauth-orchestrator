package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.client.modelapi.In;
import knemognition.heartauth.orchestrator.client.modelapi.PredictResponse;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompleteChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiClient;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.external.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteChallengeServiceImpl implements CompleteChallengeService {

    private final StatusStore<ChallengeState> challengeStore;
    private final ModelApiClient modelEvaluation;

    @Override
    public StatusResponse complete(UUID challengeId,
                                   ChallengeCompleteRequest req
    ) {
        challengeStore.getStatus(challengeId).
                orElseThrow(() -> new IllegalStateException("pairing_not_found_or_expired"));

        StatusChange.StatusChangeBuilder statusChangeBuilder = StatusChange.builder().id(challengeId);

        In in = new In().anything("placeholder"); // TODO: populate with MODEL DATA
        PredictResponse prediction;
        try {
            prediction = modelEvaluation.predict(in);
            log.info("Called model for prediction.");
        } catch (Exception e) {
            log.warn("model-api call failed for challenge {}", challengeId, e);
            challengeStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED).build());
            return new StatusResponse(knemognition.heartauth.orchestrator.external.model.FlowStatus.DENIED);
        }

        boolean approved = prediction != null && Boolean.TRUE.equals(prediction.getPrediction());

        if (approved) {
            challengeStore.setStatus(statusChangeBuilder.status(FlowStatus.APPROVED).build());
            return new StatusResponse(knemognition.heartauth.orchestrator.external.model.FlowStatus.APPROVED);
        }
        challengeStore.setStatus(statusChangeBuilder.status(FlowStatus.DENIED).build());
        return new StatusResponse(knemognition.heartauth.orchestrator.external.model.FlowStatus.DENIED);
    }
}
