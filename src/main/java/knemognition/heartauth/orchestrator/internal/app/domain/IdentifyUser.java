package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

/**
 * Command object representing the intent to identify a user
 * within a whole system.
 */
@Value
@Builder
public class IdentifyUser {
    UUID userId;
    UUID tenantId;
}
