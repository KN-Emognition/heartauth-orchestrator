package knemognition.heartauth.orchestrator.internal.config.errorhandling.exception;

public class ApiKeyException extends RuntimeException {
    public ApiKeyException(String message) {
        super(message);
    }
}
