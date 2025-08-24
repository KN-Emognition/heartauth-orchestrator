package knemognition.heartauth.orchestrator.internal.app.ports.in;

import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;

public interface InternalChallengeService {
    ChallengeCreateResponse createAndDispatch(ChallengeCreateRequest req);
}
