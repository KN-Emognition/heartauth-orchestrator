package knemognition.heartauth.orchestrator.interfaces.internal.app.ports.out;

import knemognition.heartauth.orchestrator.interfaces.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.interfaces.internal.app.domain.CreatedFlowResult;

public interface InternalPairingStore {
    CreatedFlowResult createPairing(CreatePairing state);
}
