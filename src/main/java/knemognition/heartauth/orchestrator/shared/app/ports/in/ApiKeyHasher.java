package knemognition.heartauth.orchestrator.shared.app.ports.in;

public interface ApiKeyHasher {
    String hash(String plaintext);
}