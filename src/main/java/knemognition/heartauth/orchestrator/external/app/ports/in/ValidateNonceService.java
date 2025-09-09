package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;

public interface ValidateNonceService {
    void validate(ValidateNonce validateNonce);
}
