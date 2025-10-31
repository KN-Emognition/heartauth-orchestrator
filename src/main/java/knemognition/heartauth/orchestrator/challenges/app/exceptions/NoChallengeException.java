package knemognition.heartauth.orchestrator.challenges.app.exceptions;

public class NoChallengeException extends RuntimeException {
    public NoChallengeException(String message) {
        super(message);
    }
}
