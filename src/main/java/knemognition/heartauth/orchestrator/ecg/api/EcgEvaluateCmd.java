package knemognition.heartauth.orchestrator.ecg.api;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class EcgEvaluateCmd {
    UUID userId;
    UUID correlationId;
    List<Float> testEcg;
}
