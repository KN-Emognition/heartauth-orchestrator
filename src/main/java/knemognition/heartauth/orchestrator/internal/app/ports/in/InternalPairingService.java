package knemognition.heartauth.orchestrator.internal.app.ports.in;

import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreatePairingRequestDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreatePairingResponseDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.StatusResponseDto;

import java.util.UUID;

/**
 * Internal API for creating and querying device/user pairing flows.
 */
public interface InternalPairingService {

    /**
     * Create a new pairing for the given tenant and user.
     * <p>
     * Behavior:
     * <ul>
     *   <li>Fails if the user already exists in the main store (treated as "already paired").</li>
     *   <li>Clamps the requested TTL to configured min/max, or uses the default if null.</li>
     *   <li>Generates a unique JTI and signs an ES256 JWT with {@code tenantId} and {@code userId} claims.</li>
     *   <li>Persists pairing state and returns the token and JTI to the caller.</li>
     * </ul>
     *
     * @param req      the pairing creation request (must include target user ID; may include a TTL)
     * @param tenantId the tenant under which the pairing is created
     * @return {@link CreatePairingResponseDto} containing the JTI and signed JWT
     * @throws IllegalStateException if the user already exists (i.e., pairing already present)
     */
    CreatePairingResponseDto createPairing(CreatePairingRequestDto req, UUID tenantId);

    /**
     * Get the status of a pairing by its ID and tenant.
     * <p>
     * Returns {@code Not found} if the pairing does not exist, is expired, or the tenant does not match.
     *
     * @param id       the pairing ID (JTI)
     * @param tenantId the tenant attempting to access the pairing
     * @return a {@link StatusResponseDto} containing the current status or {@code Not found}
     */
    StatusResponseDto getPairingStatus(UUID id, UUID tenantId);
}
