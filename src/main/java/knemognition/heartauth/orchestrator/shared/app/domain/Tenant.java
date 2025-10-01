package knemognition.heartauth.orchestrator.shared.app.domain;


import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Tenant {
    UUID id;
    UUID externalId;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
