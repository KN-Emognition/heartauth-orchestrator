package knemognition.heartauth.orchestrator.tenant.api;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class TenantRead {
    UUID tenantId;
    UUID id;
}
