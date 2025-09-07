package knemognition.heartauth.orchestrator.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NonceUtils {
    public static String createNonce(SecureRandom secureRandom, int length) {
        byte[] b = new byte[length];
        secureRandom.nextBytes(b);
        return Base64.getEncoder().encodeToString(b);
    }
}
