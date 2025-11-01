package knemognition.heartauth.orchestrator.modelapi.api;

public interface ModelApiCallbackApi {
    void handle(CompleteChallengeWithPredictionResultCmd payload);
}
