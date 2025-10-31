package knemognition.heartauth.orchestrator.tenants.app.ports;

import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import knemognition.heartauth.orchestrator.tenants.domain.TenantApiKey;

import java.util.Optional;
import java.util.UUID;

public interface TenantStore {

    void createTenantWithApiKey(Tenant tenant, TenantApiKey tenantApiKey);

    Optional<Tenant> get(UUID id);

    Optional<Tenant> getTenantByApiKey(String keyHash);
}