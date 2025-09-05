package knemognition.heartauth.orchestrator.internal.app.ports.in;

import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateResponse;

public interface CreatePairingService {
    PairingCreateResponse create(PairingCreateRequest req);
}
