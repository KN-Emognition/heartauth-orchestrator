package knemognition.heartauth.orchestrator.interfaces.internal.config.errorhandling.exception;

public class FirebaseSendException extends RuntimeException {
    public FirebaseSendException(String message) {
        super(message);
    }
}
