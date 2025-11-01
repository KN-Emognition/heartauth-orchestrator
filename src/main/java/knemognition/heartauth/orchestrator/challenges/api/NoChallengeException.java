package knemognition.heartauth.orchestrator.challenges.api;

public class NoChallengeException extends RuntimeException {
    public NoChallengeException(String message) {
        super(message);
    }
}
