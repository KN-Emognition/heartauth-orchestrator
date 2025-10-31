package knemognition.heartauth.orchestrator.challenges.app.exceptions;

public class FirebaseSendException extends RuntimeException {
    public FirebaseSendException(String message) {
        super(message);
    }
}
