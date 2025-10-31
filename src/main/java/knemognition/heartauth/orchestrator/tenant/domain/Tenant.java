package knemognition.heartauth.orchestrator.tenant.domain;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Tenant {
    UUID tenantId;
    UUID id;
}
