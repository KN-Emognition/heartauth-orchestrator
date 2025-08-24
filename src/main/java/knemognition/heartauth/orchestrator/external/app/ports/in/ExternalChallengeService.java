package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.model.ChallengeCompleteRequest;
import knemognition.heartauth.orchestrator.external.model.ChallengeStatusResponse;

import java.util.UUID;

public interface ExternalChallengeService {
    ChallengeStatusResponse complete(UUID challengeId,
                                     ChallengeCompleteRequest req,
                                     String dpopHeader
    );
}
