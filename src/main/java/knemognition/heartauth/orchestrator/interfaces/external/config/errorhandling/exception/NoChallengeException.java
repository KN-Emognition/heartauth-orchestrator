package knemognition.heartauth.orchestrator.interfaces.external.config.errorhandling.exception;

public class NoChallengeException extends RuntimeException {
    public NoChallengeException(String message) {
        super(message);
    }
}
