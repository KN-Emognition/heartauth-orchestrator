package knemognition.heartauth.orchestrator.external.app.ports.out;

import java.util.Optional;
import java.util.UUID;

public interface GetFlowStore<T> {
    Optional<T> getFlow(UUID id);
}