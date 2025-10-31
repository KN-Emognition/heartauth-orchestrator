package knemognition.heartauth.orchestrator.interfaces.external.app.domain;

import lombok.Builder;
import lombok.Value;

import java.security.interfaces.ECPublicKey;

@Value
@Builder
public class ValidateNonce {
    String nonce;
    String signature;
    ECPublicKey pub;
}
