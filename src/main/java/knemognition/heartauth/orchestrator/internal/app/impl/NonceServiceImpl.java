package knemognition.heartauth.orchestrator.internal.app.impl;

import knemognition.heartauth.orchestrator.internal.app.ports.in.NonceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class NonceServiceImpl implements NonceService {

    private final SecureRandom secureRandom;

    /**
     * {@inheritDoc}
     */
    @Override
    public String createNonce(int length) {
        byte[] b = new byte[length];
        secureRandom.nextBytes(b);
        return Base64.getEncoder()
                .encodeToString(b);
    }
}
