package knemognition.heartauth.orchestrator.external.config.errorhandling.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
