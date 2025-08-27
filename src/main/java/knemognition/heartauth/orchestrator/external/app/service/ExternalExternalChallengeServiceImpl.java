package knemognition.heartauth.orchestrator.external.app.service;

import knemognition.heartauth.orchestrator.client.modelapi.In;
import knemognition.heartauth.orchestrator.client.modelapi.PredictResponse;
import knemognition.heartauth.orchestrator.external.app.ports.in.ExternalChallengeService;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiClient;
import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.external.model.ChallengeStatusResponse;
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
public class ExternalExternalChallengeServiceImpl implements ExternalChallengeService {

    private final ChallengeStore challengeStore;
    private final ModelApiClient modelEvaluation;

    public ChallengeStatusResponse complete(UUID challengeId,
                                            ChallengeCompleteRequest req,
                                            String dpopHeader
    ) {
        Optional<ChallengeState> maybe = challengeStore.get(challengeId);
        if (maybe.isEmpty()) {
            return new ChallengeStatusResponse()
                    .status(FlowStatus.EXPIRED);
        }
        ChallengeState st = maybe.get();
        long now = Instant.now().getEpochSecond();
        if (st.getExp() != null && st.getExp() <= now) {
            return new ChallengeStatusResponse()
                    .status(FlowStatus.EXPIRED);
        }

        // TODO: verify assertion JWT signature, challengeId/nonce/device binding, DPoP, etc.

        In in = new In().anything("placeholder"); // TODO: populate with MODEL DATA
        System.out.println(in.getAnything());
        PredictResponse prediction;
        try {
            prediction = modelEvaluation.predict(in);
        } catch (Exception e) {
            log.warn("model-api call failed for challenge {}", challengeId, e);
            // If the model call fails, you can choose to deny or keep pending.
            // Here we deny with 'policy' reason to be explicit.
            challengeStore.changeStatus(challengeId, FlowStatus.DENIED, "policy");
            return new ChallengeStatusResponse()
                    .status(FlowStatus.DENIED)
                    .reason(ChallengeStatusResponse.ReasonEnum.POLICY);
        }

        boolean approved = prediction != null && Boolean.TRUE.equals(prediction.getPrediction());

        if (approved) {
            challengeStore.changeStatus(challengeId, FlowStatus.APPROVED, null);
            return new ChallengeStatusResponse()
                    .status(FlowStatus.APPROVED);
        } else {
            challengeStore.changeStatus(challengeId, FlowStatus.DENIED, "low_score");
            return new ChallengeStatusResponse()
                    .status(FlowStatus.DENIED)
                    .reason(ChallengeStatusResponse.ReasonEnum.LOW_SCORE);
        }
    }
}
