package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CompleteChallengeWithPredictionResultCmd {
    UUID correlationId;
    Boolean ok;
    Boolean prediction;
    Float score;
    String error;
}