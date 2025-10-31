package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * Command object representing the intent to create a challenge for a user
 * within a tenant context.
 */
@Value
@Builder
public class CreateChallengeCmd {
    UUID tenantId;
    UUID userId;

    String nonceB64;
    String userPublicKey;

    Long ttlSeconds;
}
