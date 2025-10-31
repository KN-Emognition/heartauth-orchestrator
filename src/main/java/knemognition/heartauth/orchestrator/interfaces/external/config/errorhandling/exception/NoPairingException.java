package knemognition.heartauth.orchestrator.interfaces.external.config.errorhandling.exception;

public class NoPairingException extends RuntimeException {
    public NoPairingException(String message) {
        super(message);
    }
}
