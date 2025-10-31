package knemognition.heartauth.orchestrator.users.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class IdentifiableUserCmd {
    UUID userId;
    UUID tenantId;
}

