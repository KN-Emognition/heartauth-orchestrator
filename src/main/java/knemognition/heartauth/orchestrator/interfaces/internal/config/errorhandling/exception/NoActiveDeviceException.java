package knemognition.heartauth.orchestrator.interfaces.internal.config.errorhandling.exception;

public class NoActiveDeviceException extends RuntimeException {
    public NoActiveDeviceException(String message) {
        super(message);
    }
}
