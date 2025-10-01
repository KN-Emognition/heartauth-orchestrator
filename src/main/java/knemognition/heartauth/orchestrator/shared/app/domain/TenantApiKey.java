package knemognition.heartauth.orchestrator.shared.app.domain;


import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TenantApiKey {
    UUID id;
    UUID tenantId;
    String keyHash;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    OffsetDateTime lastUsedAt;
    OffsetDateTime expiresAt;
    OffsetDateTime revokedAt;
}
