package knemognition.heartauth.orchestrator.tenants.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetTenantHandler {
    private final TenantStore tenantStore;
    private final TenantMapper tenantMapper;

    public Optional<TenantRead> handle(UUID tenantId) {
        log.info("[TENANT] Retrieving tenant by apiKey");
        return tenantStore.get(tenantId)
                .map(tenantMapper::toReadModel);
    }
}
