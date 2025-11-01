package knemognition.heartauth.orchestrator.users.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserRead {
    UUID id;
    UUID userId;
    UUID tenantId;
}
