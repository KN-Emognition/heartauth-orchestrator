package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.client.modelapi.In;
import knemognition.heartauth.orchestrator.client.modelapi.PredictResponse;
import knemognition.heartauth.orchestrator.external.app.ports.in.CompleteChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiClient;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.external.model.StatusResponse;
import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.ports.out.ChallengeStore;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteChallengeServiceImpl implements CompleteChallengeService {

    private final ChallengeStore challengeStore;
    private final ModelApiClient modelEvaluation;

    public StatusResponse complete(UUID challengeId,
                                            ChallengeCompleteRequest req,
                                            String dpopHeader
    ) {
        Optional<ChallengeState> maybe = challengeStore.get(challengeId);
        if (maybe.isEmpty()) {
            return new StatusResponse()
                    .status(FlowStatus.EXPIRED);
        }
        ChallengeState st = maybe.get();
        long now = Instant.now().getEpochSecond();
        if (st.getExp() != null && st.getExp() <= now) {
            return new StatusResponse()
                    .status(FlowStatus.EXPIRED);
        }

        // TODO: verify assertion JWT signature, challengeId/nonce/device binding, DPoP, etc.

        In in = new In().anything("placeholder"); // TODO: populate with MODEL DATA
        PredictResponse prediction;
        try {
            prediction = modelEvaluation.predict(in);
        } catch (Exception e) {
            log.warn("model-api call failed for challenge {}", challengeId, e);
            // If the model call fails, you can choose to deny or keep pending.
            // Here we deny with 'policy' reason to be explicit.
            challengeStore.changeStatus(challengeId, FlowStatus.DENIED, "policy");
            return new StatusResponse()
                    .status(FlowStatus.DENIED)
                    .reason(StatusResponse.ReasonEnum.POLICY);
        }

        boolean approved = prediction != null && Boolean.TRUE.equals(prediction.getPrediction());

        if (approved) {
            challengeStore.changeStatus(challengeId, FlowStatus.APPROVED, null);
            return new StatusResponse()
                    .status(FlowStatus.APPROVED);
        } else {
            challengeStore.changeStatus(challengeId, FlowStatus.DENIED, "low_score");
            return new StatusResponse()
                    .status(FlowStatus.DENIED)
                    .reason(StatusResponse.ReasonEnum.LOW_SCORE);
        }
    }
}
