package knemognition.heartauth.orchestrator.internal.app.ports.in;

import knemognition.heartauth.orchestrator.internal.model.StatusResponse;

import java.util.UUID;

public interface StatusService {
    StatusResponse status(UUID id);
}
