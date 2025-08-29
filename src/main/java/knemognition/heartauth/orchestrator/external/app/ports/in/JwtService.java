package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;

public interface JwtService {
    QrClaims process();
}
