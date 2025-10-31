package knemognition.heartauth.orchestrator.tenant.api;

import java.util.Optional;
import java.util.UUID;

public interface TenantApi {
    CreatedTenant create();

    Optional<TenantRead> get(UUID tenantId);
}
