package knemognition.heartauth.orchestrator.users.api;

public class NoActiveDeviceException extends RuntimeException {
    public NoActiveDeviceException(String message) {
        super(message);
    }
}
