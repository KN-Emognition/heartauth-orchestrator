package knemognition.heartauth.orchestrator.external.config.errorhandling.exception;

public class NoPairingException extends RuntimeException {
    public NoPairingException(String message) {
        super(message);
    }
}
