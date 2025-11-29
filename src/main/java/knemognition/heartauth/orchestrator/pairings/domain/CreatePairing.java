package knemognition.heartauth.orchestrator.pairings.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * Command object representing the intent to create a pairing for a user
 * within a tenant context.
 */
@Value
@Builder
public class CreatePairing {
    UUID tenantId;
    UUID userId;
    String username;
    UUID jti;
    Long ttlSeconds;
}