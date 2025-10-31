package knemognition.heartauth.orchestrator.security.api;

public class NonceValidationException extends RuntimeException {
    public NonceValidationException(String message) {
        super(message);
    }
}
