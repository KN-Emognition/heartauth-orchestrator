package knemognition.heartauth.orchestrator.internal.app.impl;

import knemognition.heartauth.orchestrator.internal.app.ports.in.KeyCreatorService;
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
public class KeyCreatorServiceImpl implements KeyCreatorService {
    @Override
    public KeyPair createEphemeralKeyPair() {
        return create();
    }

    private KeyPair create() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(new ECGenParameterSpec("secp256r1"));
            return kpg.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
