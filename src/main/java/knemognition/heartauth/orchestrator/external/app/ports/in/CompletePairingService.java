package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmResponse;

public interface CompletePairingService {
    PairingConfirmResponse complete(PairingConfirmRequest req, QrClaims claims);
}
