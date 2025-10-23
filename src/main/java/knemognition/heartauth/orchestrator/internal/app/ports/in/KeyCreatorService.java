package knemognition.heartauth.orchestrator.internal.app.ports.in;

import java.security.KeyPair;

public interface KeyCreatorService {
    KeyPair createEphemeralKeyPair();
}
