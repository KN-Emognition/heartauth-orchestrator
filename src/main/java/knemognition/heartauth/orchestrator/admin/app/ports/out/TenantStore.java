package knemognition.heartauth.orchestrator.admin.app.ports.out;

import java.util.Optional;
import java.util.UUID;

public interface TenantStore {
    Optional<UUID> findIdByExternalId(UUID externalId);

    UUID createTenant(UUID externalId);

    void storeApiKeyByExternalId(UUID tenantId, String keyHash);
}