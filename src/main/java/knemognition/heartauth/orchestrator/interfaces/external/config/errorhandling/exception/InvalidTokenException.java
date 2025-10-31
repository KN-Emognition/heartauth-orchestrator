package knemognition.heartauth.orchestrator.interfaces.external.config.errorhandling.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
