package knemognition.heartauth.orchestrator.security.app.exception;

public class NonceValidationException extends RuntimeException {
    public NonceValidationException(String message) {
        super(message);
    }
}
