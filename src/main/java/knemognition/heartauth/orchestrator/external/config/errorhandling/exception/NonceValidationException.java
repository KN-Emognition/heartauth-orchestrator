package knemognition.heartauth.orchestrator.external.config.errorhandling.exception;

public class NonceValidationException extends RuntimeException {
    public NonceValidationException(String message) {
        super(message);
    }
}
