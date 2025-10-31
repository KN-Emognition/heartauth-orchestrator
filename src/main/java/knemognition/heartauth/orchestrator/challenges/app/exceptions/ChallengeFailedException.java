package knemognition.heartauth.orchestrator.challenges.app.exceptions;

public class ChallengeFailedException extends RuntimeException {
    public ChallengeFailedException(String message) {
        super(message);
    }
}
