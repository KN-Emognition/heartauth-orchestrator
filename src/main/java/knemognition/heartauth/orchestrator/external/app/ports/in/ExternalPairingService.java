package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmResponse;
import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
import knemognition.heartauth.orchestrator.external.model.PairingInitResponse;

public interface ExternalPairingService {
    PairingConfirmResponse confirmPairing(PairingConfirmRequest pairingConfirmRequest);
    PairingInitResponse initPairing(PairingInitRequest pairingInitRequest);
}
