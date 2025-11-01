package knemognition.heartauth.orchestrator.challenges.domain;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;


@Value
@Builder
public class ChallengePushMessage {
    UUID challengeId;
    MessageType type;
    String publicKey;
    String nonce;
    Long ttl;
    Long exp;
}

