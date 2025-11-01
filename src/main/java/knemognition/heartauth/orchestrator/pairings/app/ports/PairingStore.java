package knemognition.heartauth.orchestrator.pairings.app.ports;

import knemognition.heartauth.orchestrator.pairings.api.NoPairingException;
import knemognition.heartauth.orchestrator.pairings.domain.*;

import java.util.Optional;
import java.util.UUID;

public interface PairingStore {
    CreatedPairingResult createPairing(CreatePairing state);

    Optional<PairingState> getFlow(UUID id);

    void enrichWithDeviceData(EnrichDeviceData req);

    boolean setStatus(StatusChange statusChange);

    default void setStatusOrThrow(StatusChange change) {
        if (!setStatus(change)) {
            throw new NoPairingException("Status change not set");
        }
    }
}
