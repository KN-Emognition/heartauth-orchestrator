package knemognition.heartauth.orchestrator.external.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;

public interface ExternalChallengeStore {
    boolean setStatus(StatusChange statusChange);

    default void setStatusOrThrow(StatusChange change) {
        if (!setStatus(change)) {
            throw new StatusServiceException("Status change not set");
        }
    }
}
