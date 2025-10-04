package knemognition.heartauth.orchestrator.external.app.ports.in;

import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
import java.util.UUID;

/**
 * Defines the operations for completing external authentication challenges.
 * <p>
 * This service is responsible for finalizing a challenge flow based on a given challenge ID
 * and a {@link CompleteChallengeRequestDto}. Implementations are expected to validate, decrypt,
 * and process incoming challenge data, and update the challenge state accordingly.
 * </p>
 */
public interface ExternalChallengeService {

    /**
     * Completes a challenge identified by the given challenge ID.
     * <p>
     * The process involves:
     * <ul>
     *   <li>Retrieving the current challenge state.</li>
     *   <li>Validating nonce information to prevent replay attacks.</li>
     *   <li>Decrypting and verifying incoming JWT/JWE payloads.</li>
     *   <li>Calling external model-api to get prediction.</li>
     *   <li>Updating the challenge state to APPROVED or DENIED based on prediction results.</li>
     * </ul>
     * </p>
     *
     * @param challengeId the unique identifier of the challenge
     * @param req the request payload containing the data required to complete the challenge
     * @throws knemognition.heartauth.orchestrator.external.config.errorhandling.exception.NoChallengeException
     *         if the challenge cannot be found, has expired, or has already been completed
     * @throws knemognition.heartauth.orchestrator.external.config.errorhandling.exception.ChallengeFailedException
     *         if the challenge fails due to missing reference data, invalid ECG validation,
     *         or errors accessing the model API
     */
    void completeChallenge(UUID challengeId, CompleteChallengeRequestDto req);
}
