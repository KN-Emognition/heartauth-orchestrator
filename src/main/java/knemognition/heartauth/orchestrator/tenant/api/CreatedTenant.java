package knemognition.heartauth.orchestrator.tenant.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreatedTenant {
    UUID id;
    UUID apiKey;
}
