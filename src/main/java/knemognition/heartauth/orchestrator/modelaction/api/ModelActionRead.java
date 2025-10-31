package knemognition.heartauth.orchestrator.modelaction.api;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ModelActionRead {
    UUID correlationId;
    String payload;
}

