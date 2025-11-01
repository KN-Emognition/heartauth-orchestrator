package knemognition.heartauth.orchestrator.security.app.handlers.createEphemeralKeyPair;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateEphemeralKeyPairHandlerImpl implements CreateEphemeralKeyPairHandler {

    public KeyPair handle() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(new ECGenParameterSpec("secp256r1"));
            return kpg.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
