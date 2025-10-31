package knemognition.heartauth.orchestrator.tenants.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreatedTenant {
    UUID tenantId;
    UUID apiKey;
}
