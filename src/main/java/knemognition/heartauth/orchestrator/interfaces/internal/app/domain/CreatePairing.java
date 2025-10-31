package knemognition.heartauth.orchestrator.interfaces.internal.app.domain;

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

    UUID jti;

    Long ttlSeconds;
}