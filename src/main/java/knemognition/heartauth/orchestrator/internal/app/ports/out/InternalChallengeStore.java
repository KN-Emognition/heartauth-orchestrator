package knemognition.heartauth.orchestrator.internal.app.ports.out;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;

import java.util.UUID;


public interface InternalChallengeStore {
    CreatedFlowResult createChallenge(CreateChallenge state);

    ChallengeState getChallengeStateByCorrelationId(UUID correlationId);

    boolean setStatus(StatusChange statusChange);

    default void setStatusOrThrow(StatusChange change) {
        if (!setStatus(change)) {
            throw new StatusServiceException("Status change not set");
        }
    }
}

