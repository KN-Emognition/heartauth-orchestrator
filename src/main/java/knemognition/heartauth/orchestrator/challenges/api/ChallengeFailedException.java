package knemognition.heartauth.orchestrator.challenges.api;

public class ChallengeFailedException extends RuntimeException {
    public ChallengeFailedException(String message) {
        super(message);
    }
}
