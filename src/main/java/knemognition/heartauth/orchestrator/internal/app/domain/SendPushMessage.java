package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * Command object representing the intent to send a push message.
 */
@Value
@Builder
public class SendPushMessage {
    UUID challengeId;
    String type;

    String publicKey;
    String nonce;

    Long ttl;
    Long exp;
}
