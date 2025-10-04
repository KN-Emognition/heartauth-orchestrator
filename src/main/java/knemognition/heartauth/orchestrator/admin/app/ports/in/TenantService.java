package knemognition.heartauth.orchestrator.admin.app.ports.in;

import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.CreateTenantResponseDto;

/**
 * Service for managing tenants in the system.
 */
public interface TenantService {

    /**
     * Registers a new tenant with a server-generated tenantId and API key.
     * Returns plaintext apiKey once; only its hash is stored.
     */
    CreateTenantResponseDto register();
}
