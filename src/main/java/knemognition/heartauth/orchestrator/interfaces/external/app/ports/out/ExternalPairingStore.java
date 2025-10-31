package knemognition.heartauth.orchestrator.interfaces.external.app.ports.out;

import knemognition.heartauth.orchestrator.interfaces.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException;

public interface ExternalPairingStore {
    void enrichWithDeviceData(EnrichDeviceData req);

    boolean setStatus(StatusChange statusChange);

    default void setStatusOrThrow(StatusChange change) {
        if (!setStatus(change)) {
            throw new StatusServiceException("Status change not set");
        }
    }
}
