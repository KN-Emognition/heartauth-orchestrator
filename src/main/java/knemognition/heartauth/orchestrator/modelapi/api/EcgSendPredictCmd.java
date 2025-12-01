package knemognition.heartauth.orchestrator.modelapi.api;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class EcgSendPredictCmd {
    UUID correlationId;
    String userCompoundId;
    List<Float> testEcg;
    List<List<Float>> refEcg;
}
