package knemognition.heartauth.orchestrator.internal.config.errorhandling.exception;

public class NoActiveDeviceException extends RuntimeException {
    public NoActiveDeviceException() {
        super("No active device for given user.");
    }
}
