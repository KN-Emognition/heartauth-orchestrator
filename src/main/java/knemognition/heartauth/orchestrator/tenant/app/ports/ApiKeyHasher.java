package knemognition.heartauth.orchestrator.tenant.app.ports;

public interface ApiKeyHasher {
    String hash(String plaintext);
}