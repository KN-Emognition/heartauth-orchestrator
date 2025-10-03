package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * Command object representing the intent to create a challenge for a user
 * within a tenant context.
 */
@Value
@Builder
public class CreateChallenge {
    UUID tenantId;
    UUID userId;

    String nonceB64;
    String ephemeralPrivateKey;
    String userPublicKey;

    Long ttlSeconds;
}
