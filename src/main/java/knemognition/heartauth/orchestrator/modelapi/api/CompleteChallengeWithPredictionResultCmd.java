package knemognition.heartauth.orchestrator.modelapi.api;

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