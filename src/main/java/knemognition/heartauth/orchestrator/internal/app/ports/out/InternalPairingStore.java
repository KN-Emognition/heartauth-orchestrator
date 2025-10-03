package knemognition.heartauth.orchestrator.internal.app.ports.out;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;

public interface InternalPairingStore {
    CreatedFlowResult createPairing(CreatePairing state);
}
