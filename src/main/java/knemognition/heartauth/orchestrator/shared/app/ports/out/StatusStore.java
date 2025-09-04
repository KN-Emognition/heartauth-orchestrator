package knemognition.heartauth.orchestrator.shared.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;

import java.util.Optional;
import java.util.UUID;


public interface StatusStore<T> {
    boolean setStatus(StatusChange statusChange);

    Optional<FlowStatusDescription> getStatus(UUID id);
}
