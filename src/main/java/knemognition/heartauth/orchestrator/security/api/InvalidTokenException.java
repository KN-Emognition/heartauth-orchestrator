package knemognition.heartauth.orchestrator.security.api;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
