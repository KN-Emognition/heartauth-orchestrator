package knemognition.heartauth.orchestrator.security.api;

import lombok.Builder;
import lombok.Value;

import java.security.interfaces.ECPublicKey;

@Value
@Builder
public class ValidateNonceCmd {
    String nonce;
    String signature;
    String pub;
}
