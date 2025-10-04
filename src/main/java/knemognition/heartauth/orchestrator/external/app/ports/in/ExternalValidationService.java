package knemognition.heartauth.orchestrator.external.app.ports.in;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import knemognition.heartauth.orchestrator.external.app.domain.DecryptJwe;
import knemognition.heartauth.orchestrator.external.app.domain.ValidateNonce;

import java.text.ParseException;

/**
 * Defines cryptographic validation operations for external authentication and pairing flows.
 * <p>
 * This service provides utilities to:
 * <ul>
 *   <li>Validate signed nonces to prevent replay attacks.</li>
 *   <li>Decrypt and verify JSON Web Encryption (JWE) payloads containing signed JWTs.</li>
 *   <li>Validate device-provided public keys in PEM format.</li>
 * </ul>
 * </p>
 */
public interface ExternalValidationService {

    /**
     * Validates a signed nonce against a public key.
     * <p>
     * Used to confirm that the provided nonce and its ES256 signature
     * match the expected public key. Prevents replay or tampering of authentication flows.
     * </p>
     *
     * @param validateNonce the request object containing the nonce string,
     *                      its signature, and the public key to verify against
     *
     * @throws knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NonceValidationException
     *         if the signature is invalid, incorrectly formatted, or verification fails
     */
    void validateNonce(ValidateNonce validateNonce);

    /**
     * Decrypts a JWE payload and verifies the embedded signed JWT.
     * <p>
     * The decryption uses the recipient’s private key, and the verification step
     * checks the ES256 signature against the sender’s public key.
     * </p>
     *
     * @param jwt the decryption request containing the JWE string, recipient private key,
     *            and sender public key
     * @return the decrypted {@link JWTClaimsSet} if decryption and verification succeed
     *
     * @throws JOSEException if the JWE/JWT signature verification fails or unsupported algorithms are encountered
     * @throws ParseException if the JWE or JWT payload cannot be parsed
     */
    JWTClaimsSet decryptAndVerifyJwe(DecryptJwe jwt) throws JOSEException, ParseException;

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
}
