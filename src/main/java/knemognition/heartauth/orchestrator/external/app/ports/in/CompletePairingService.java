package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;

public interface CompletePairingService {
    void complete(PairingConfirmRequest req, QrClaims claims);
}
