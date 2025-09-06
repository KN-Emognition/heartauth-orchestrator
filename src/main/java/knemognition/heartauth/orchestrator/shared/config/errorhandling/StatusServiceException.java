package knemognition.heartauth.orchestrator.shared.config.errorhandling;

public class StatusServiceException extends RuntimeException {
    public StatusServiceException(String message) {
        super(message);
    }
}
