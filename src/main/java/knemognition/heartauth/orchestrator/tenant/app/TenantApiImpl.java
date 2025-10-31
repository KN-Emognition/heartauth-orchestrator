package knemognition.heartauth.orchestrator.tenant.app;

import knemognition.heartauth.orchestrator.tenant.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenant.api.TenantApi;
import knemognition.heartauth.orchestrator.tenant.api.TenantRead;
import knemognition.heartauth.orchestrator.tenant.app.handlers.CreateTenantHandler;
import knemognition.heartauth.orchestrator.tenant.app.handlers.GetTenantHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantApiImpl implements TenantApi {
    private final CreateTenantHandler createTenantHandler;
    private final GetTenantHandler existTenantHandler;

    @Override
    public CreatedTenant create() {
        return createTenantHandler.handle();
    }

    @Override
    public Optional<TenantRead> get(UUID tenantId) {
        return existTenantHandler.handle(tenantId);
    }
}
