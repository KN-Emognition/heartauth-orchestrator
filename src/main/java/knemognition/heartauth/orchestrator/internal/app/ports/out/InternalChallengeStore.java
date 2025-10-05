package knemognition.heartauth.orchestrator.internal.app.ports.out;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;


public interface InternalChallengeStore {
    CreatedFlowResult createChallenge(CreateChallenge state);
}

