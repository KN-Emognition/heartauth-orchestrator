package knemognition.heartauth.orchestrator.internal.app.ports.in;

/**
 * Service for generating nonces (numbers used once).
 * <p>
 * Nonces are used to ensure uniqueness and prevent replay attacks
 * in security protocols or ephemeral flows (e.g., pairing, challenge creation).
 */
public interface NonceService {

    /**
     * Generate a cryptographically secure, random nonce of the given length.
     * <p>
     * The nonce is returned as a Base64-encoded string.
     *
     * @param length number of random bytes to generate before Base64 encoding
     * @return a Base64 string representation of the generated nonce
     * @throws IllegalArgumentException if {@code length <= 0}
     */
    String createNonce(int length);
}
