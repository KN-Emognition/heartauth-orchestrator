package knemognition.heartauth.orchestrator.tenant.app.handlers;

import knemognition.heartauth.orchestrator.tenant.api.TenantRead;
import knemognition.heartauth.orchestrator.tenant.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenant.app.ports.TenantStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTenantHandler {
    private final TenantStore tenantStore;
    private final TenantMapper tenantMapper;

    public Optional<TenantRead> handle(UUID tenantId) {
        return tenantStore.get(tenantId)
                .map(tenantMapper::toReadModel);
    }
}
