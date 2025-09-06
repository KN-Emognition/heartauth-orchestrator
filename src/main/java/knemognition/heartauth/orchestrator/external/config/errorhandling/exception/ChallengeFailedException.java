package knemognition.heartauth.orchestrator.external.config.errorhandling.exception;

public class ChallengeFailedException extends RuntimeException {
    public ChallengeFailedException(String message) {
        super(message);
    }
}
