package knemognition.heartauth.orchestrator.interfaces.external.config.errorhandling.exception;

public class NonceValidationException extends RuntimeException {
    public NonceValidationException(String message) {
        super(message);
    }
}
