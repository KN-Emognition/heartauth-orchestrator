package knemognition.heartauth.orchestrator.tenants.api;

import org.springframework.modulith.NamedInterface;

import java.util.Optional;
import java.util.UUID;

@NamedInterface
public interface TenantsModule {
    CreatedTenant create();

    Optional<TenantRead> get(UUID tenantId);

    Optional<TenantRead> getByApiKey(String apiKey);
}
