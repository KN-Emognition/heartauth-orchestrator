package knemognition.heartauth.orchestrator.security;

public class NonceValidationException extends RuntimeException {
    public NonceValidationException(String message) {
        super(message);
    }
}
