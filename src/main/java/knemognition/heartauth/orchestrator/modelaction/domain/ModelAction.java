package knemognition.heartauth.orchestrator.modelaction.domain;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ModelAction {
    UUID correlationId;
    String payload;
}

