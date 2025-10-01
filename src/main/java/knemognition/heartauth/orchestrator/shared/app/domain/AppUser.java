package knemognition.heartauth.orchestrator.shared.app.domain;


import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class AppUser {
    UUID id;
    UUID tenantId;
    UUID userId;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
