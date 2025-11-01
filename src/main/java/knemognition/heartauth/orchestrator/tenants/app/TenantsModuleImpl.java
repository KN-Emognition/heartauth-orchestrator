package knemognition.heartauth.orchestrator.tenants.app;

import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import knemognition.heartauth.orchestrator.tenants.app.handlers.CreateTenantHandler;
import knemognition.heartauth.orchestrator.tenants.app.handlers.GetTenantByApiKeyHandler;
import knemognition.heartauth.orchestrator.tenants.app.handlers.GetTenantHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantsModuleImpl implements TenantsModule {
    private final CreateTenantHandler createTenantHandler;
    private final GetTenantHandler existTenantHandler;
    private final GetTenantByApiKeyHandler getTenantByApiKeyHandler;

    @Override
    public CreatedTenant create() {
        return createTenantHandler.handle();
    }

    @Override
    public Optional<TenantRead> get(UUID tenantId) {
        return existTenantHandler.handle(tenantId);
    }

    @Override
    public Optional<TenantRead> getByApiKey(String apiKey) {
        return getTenantByApiKeyHandler.handle(apiKey);
    }
}
