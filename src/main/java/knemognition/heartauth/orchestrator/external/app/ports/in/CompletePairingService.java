package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.model.PairingConfirmRequest;
import knemognition.heartauth.orchestrator.external.model.StatusResponse;

public interface CompletePairingService {
    StatusResponse complete(PairingConfirmRequest req, QrClaims claims);
}
