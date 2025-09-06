package knemognition.heartauth.orchestrator.external.config.errorhandling.exception;

public class NoChallengeException extends RuntimeException {
    public NoChallengeException(String message) {
        super(message);
    }
}
