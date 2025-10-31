package knemognition.heartauth.orchestrator.internal.app.ports.out;


import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.user.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;

import java.util.UUID;


public interface InternalChallengeStore {
    CreatedFlowResult createChallenge(CreateChallengeCmd state);

    ChallengeState getChallengeStateByCorrelationId(UUID correlationId);

    boolean setStatus(StatusChange statusChange);

    default void setStatusOrThrow(StatusChange change) {
        if (!setStatus(change)) {
            throw new StatusServiceException("Status change not set");
        }
    }
}

