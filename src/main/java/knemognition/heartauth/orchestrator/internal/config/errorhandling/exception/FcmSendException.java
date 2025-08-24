package knemognition.heartauth.orchestrator.internal.config.errorhandling.exception;

public class FcmSendException extends RuntimeException {
    public FcmSendException(String message) {
        super(message);
    }
}
