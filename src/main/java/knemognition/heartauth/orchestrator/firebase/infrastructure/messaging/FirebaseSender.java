package knemognition.heartauth.orchestrator.firebase.infrastructure.messaging;

public interface FirebaseSender {
    <T> void sendMessage(String token, T message);
}
