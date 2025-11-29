package knemognition.heartauth.orchestrator.refdatacollect.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateRefDataCollectProcessCmd {
    UUID tenantId;
    UUID userId;
}
