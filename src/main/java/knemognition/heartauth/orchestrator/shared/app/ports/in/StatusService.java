package knemognition.heartauth.orchestrator.shared.app.ports.in;

import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;

import java.util.UUID;

public interface StatusService<T> {
    StatusResponse status(UUID id);

    boolean setStatus(StatusChange statusChange);
}
