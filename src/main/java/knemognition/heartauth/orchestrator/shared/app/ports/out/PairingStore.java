package knemognition.heartauth.orchestrator.shared.app.ports.out;

import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface PairingStore {
    void create(PairingState st, Duration ttl);

    Optional<PairingState> get(UUID id);

    boolean changeStatus(UUID id, FlowStatus newStatus, String reason);
}
