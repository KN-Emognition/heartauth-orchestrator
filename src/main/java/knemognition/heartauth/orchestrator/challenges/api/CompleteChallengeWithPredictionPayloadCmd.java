package knemognition.heartauth.orchestrator.challenges.api;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CompleteChallengeWithPredictionPayloadCmd {
    UUID challengeId;
    String dataToken;
    String signature;
}
