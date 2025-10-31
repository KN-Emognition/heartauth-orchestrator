package knemognition.heartauth.orchestrator.tenant.app.ports;

import knemognition.heartauth.orchestrator.tenant.domain.Tenant;
import knemognition.heartauth.orchestrator.tenant.domain.TenantApiKey;

import java.util.Optional;
import java.util.UUID;

public interface TenantStore {

    void createTenantWithApiKey(Tenant tenant, TenantApiKey tenantApiKey);

    Optional<Tenant> get(UUID id);
}