package knemognition.heartauth.orchestrator.shared.app.domain;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class TenantApiKey {
    UUID tenantId;
    String keyHash;
}
