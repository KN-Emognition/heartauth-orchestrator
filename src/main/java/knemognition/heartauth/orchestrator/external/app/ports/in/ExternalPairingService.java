package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.InitPairingResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.QrCodeClaims;

/**
 * Defines the contract for managing external device-user pairing flows.
 * <p>
 * Pairing flows allow a device (e.g., wearable or sensor) to be securely associated
 * with a user account through an initialization and confirmation process.
 * </p>
 */
public interface ExternalPairingService {

    /**
     * Initializes a pairing process for a device using a {@link InitPairingRequestDto}.
     * <p>
     * Flow involves:
     * <ul>
     *   <li>Validates the device's provided public key.</li>
     *   <li>Generates a nonce for challenge-response validation.</li>
     *   <li>Validates that the pairing state exists and is in {@code CREATED} status.</li>
     *   <li>Enriches pairing state with device data and updates status to {@code PENDING}.</li>
     *   <li>Returns response with nonce and expiry.</li>
     * </ul>
     * </p>
     *
     * @param req the pairing initialization request containing the device’s public key and metadata
     * @param claims QR code claims containing pairing ID (JTI), tenant ID, and expiry
     * @return a {@link InitPairingResponseDto} containing the generated nonce and expiration timestamp
     *
     * @throws knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException
     *         if the pairing state is missing or not in {@code CREATED} status
     * @throws knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoPairingException
     *         if the provided tenant ID does not match the pairing state
     */
    InitPairingResponseDto initPairing(InitPairingRequestDto req, QrCodeClaims claims);

    /**
     * Completes a pairing process using a {@link CompletePairingRequestDto}.
     * <p>
     * Typical flow:
     * <ul>
     *   <li>Retrieves the pairing state using QR code claims.</li>
     *   <li>Validates the provided nonce to prevent replay attacks.</li>
     *   <li>Ensures pairing is in {@code PENDING} status.</li>
     *   <li>Decrypts and verifies a JWE payload containing ECG reference data.</li>
     *   <li>Extracts pairing artifacts (ECG reference, device, user) and stores them in the main store.</li>
     *   <li>Updates the pairing state to {@code APPROVED}.</li>
     * </ul>
     * </p>
     *
     * @param req the pairing confirmation request containing device-provided encrypted data
     * @param claims QR code claims containing pairing ID (JTI), tenant ID, and expiry
     *
     * @throws knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoPairingException
     *         if pairing is not found, expired, replayed, or tenant mismatch occurs
     * @throws knemognition.heartauth.orchestrator.shared.config.errorhandling.StatusServiceException
     *         if the pairing state is not in the expected {@code PENDING} status
     */
    void completePairing(CompletePairingRequestDto req, QrCodeClaims claims);
}
