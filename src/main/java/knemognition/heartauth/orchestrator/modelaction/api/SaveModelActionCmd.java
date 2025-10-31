package knemognition.heartauth.orchestrator.modelaction.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class SaveModelActionCmd {
    UUID correlationId;
    String payload;
}
