package knemognition.heartauth.orchestrator.security.app.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateNonceHandler {
    private final SecureRandom secureRandom;

    public String handle(int length) {
        byte[] b = new byte[length];
        secureRandom.nextBytes(b);
        return Base64.getEncoder()
                .encodeToString(b);
    }
}
