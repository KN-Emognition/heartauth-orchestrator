package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.model.PairingInitRequest;
import knemognition.heartauth.orchestrator.external.model.PairingInitResponse;

public interface InitPairingService {
    PairingInitResponse init(PairingInitRequest req, QrClaims claims);
}
