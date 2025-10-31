package knemognition.heartauth.orchestrator.security.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.modulith.NamedInterface;

import java.security.KeyPair;
import java.text.ParseException;

/**
 * Defines cryptographic validation operations for authentication.
 * <p>
 * This service provides utilities to:
 * <ul>
 *   <li>Validate signed nonces to prevent replay attacks.</li>
 *   <li>Decrypt and verify JSON Web Encryption (JWE) payloads containing signed JWTs.</li>
 *   <li>Validate device-provided public keys in PEM format.</li>
 * </ul>
 * </p>
 */
@NamedInterface
public interface SecurityModule {

    /**
     * Validates a signed nonce against a public key.
     * <p>
     * Used to confirm that the provided nonce and its ES256 signature
     * match the expected public key. Prevents replay or tampering of authentication flows.
     * </p>
     *
     * @param cmd the request object containing the nonce string,
     *                      its signature, and the public key to verify against
     *
     *         if the signature is invalid, incorrectly formatted, or verification fails
     */
    void validateNonce(ValidateNonceCmd cmd);

    /**
     * Decrypts a JWE payload and verifies the embedded signed JWT.
     * <p>
     * The decryption uses the recipient’s private key, and the verification step
     * checks the ES256 signature against the sender’s public key.
     * </p>
     *
     * @param cmd the decryption request containing the JWE string, recipient private key,
     *            and sender public key
     * @return the decrypted {@link JWTClaimsSet} if decryption and verification succeed
     *
     * @throws JOSEException if the JWE/JWT signature verification fails or unsupported algorithms are encountered
     * @throws ParseException if the JWE or JWT payload cannot be parsed
     */
    <T> T decryptJwe(DecryptJweCmd<T> cmd) throws JOSEException, ParseException;

    /**
     * Validates that a given PEM-encoded public key is well-formed and usable.
     * <p>
     * Ensures that devices provide valid cryptographic material during pairing
     * or challenge flows.
     * </p>
     *
     * @param pem the PEM-encoded public key string
     * @throws IllegalArgumentException or implementation-specific exception
     *         if the PEM is invalid or cannot be mapped to a public key
     */
    void validatePublicKeyPem(String pem);

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


    KeyPair createEphemeralKeyPair();

    String hash(String payload);
}
