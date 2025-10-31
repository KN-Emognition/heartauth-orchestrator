package knemognition.heartauth.orchestrator.interfaces.internal.app.ports.in;

import knemognition.heartauth.orchestrator.interfaces.internal.api.rest.v1.model.CreateChallengeRequestDto;
import knemognition.heartauth.orchestrator.interfaces.internal.api.rest.v1.model.CreateChallengeResponseDto;
import knemognition.heartauth.orchestrator.interfaces.internal.api.rest.v1.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.interfaces.internal.config.errorhandling.exception.NoActiveDeviceException;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;

import java.util.UUID;

/**
 * Internal API for creating and querying authentication challenges.
 */
public interface InternalChallengeService {

    /**
     * Create a new challenge for the given user within the specified tenant.
     * <p>
     * Behavior:
     * <ul>
     *   <li>Checks that the user exists; otherwise throws {@link IllegalStateException}.</li>
     *   <li>Ensures the user has at least one active device; otherwise throws
     *       {@code NoActiveDeviceException} (runtime exception in the internal.impl package).</li>
     *   <li>Clamps the requested TTL in seconds to the configured min/max, falling back to the default if null.</li>
     *   <li>Generates a nonce and ephemeral key pair, persists the challenge flow, and dispatches a push message to all devices.</li>
     * </ul>
     *
     * @param req      the challenge creation request (must include the target user ID; may include a TTL)
     * @param tenantId the tenant to which the challenge belongs; used for isolation
     * @return a {@link CreateChallengeResponseDto} describing the created challenge (e.g., ID, effective TTL)
     * @throws IllegalStateException   if the user does not exist
     * @throws NoActiveDeviceException
     *                                 if the user has no active devices
     */
    CreateChallengeResponseDto createChallenge(CreateChallengeRequestDto req, UUID tenantId);

    /**
     * Get the status of a previously created challenge.
     * <p>
     * Behavior:
     * <ul>
     *   <li>Looks up the challenge state by ID.</li>
     *   <li>Returns {@code Not found} if the challenge does not exist, is expired, or if {@code tenantId}
     *       does not match the challenge's tenant.</li>
     *   <li>Otherwise returns the current status.</li>
     * </ul>
     *
     * @param id       the challenge ID
     * @param tenantId the tenant requesting access; must match the challenge tenant
     * @return a {@link StatusResponseDto} containing the status or {@code Not found}
     */
    StatusResponseDto getChallengeStatus(UUID id, UUID tenantId);

    void completeChallengeWithPrediction(UUID correlationId, PredictResponseDto to);
}
