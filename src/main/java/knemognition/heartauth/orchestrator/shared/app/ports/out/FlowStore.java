package knemognition.heartauth.orchestrator.shared.app.ports.out;


import knemognition.heartauth.orchestrator.external.model.FlowStatus;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface FlowStore<T> {
    void create(T state, Duration ttl);

    Optional<T> get(UUID id);

    boolean setStatus(UUID id, FlowStatus newStatus, String reason);
}

