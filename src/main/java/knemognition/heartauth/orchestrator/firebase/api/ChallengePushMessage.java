package knemognition.heartauth.orchestrator.firebase.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;


@Value
@Builder
public class ChallengePushMessage {
    UUID challengeId;
    String publicKey;
    String nonce;
    Long ttl;
    Long exp;
}

