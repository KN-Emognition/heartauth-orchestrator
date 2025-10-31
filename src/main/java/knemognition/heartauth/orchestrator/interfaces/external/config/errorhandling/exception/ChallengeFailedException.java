package knemognition.heartauth.orchestrator.interfaces.external.config.errorhandling.exception;

public class ChallengeFailedException extends RuntimeException {
    public ChallengeFailedException(String message) {
        super(message);
    }
}
