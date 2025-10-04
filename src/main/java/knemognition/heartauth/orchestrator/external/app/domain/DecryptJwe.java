package knemognition.heartauth.orchestrator.external.app.domain;


import lombok.Builder;
import lombok.Value;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Value
@Builder
public class DecryptJwe {
    String jwe;
    ECPublicKey senderPublicKey;
    ECPrivateKey recipientPrivateKey;
}
