package knemognition.heartauth.orchestrator.tenants.app.handlers;

import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetTenantByApiKeyHandler {

    private final TenantStore tenantStore;
    private final TenantMapper tenantMapper;

    public Optional<TenantRead> handle(String apiKey) {
        log.info("[TENANT] Retrieving tenant by apiKey");
        return tenantStore.getTenantByApiKey(apiKey)
                .map(tenantMapper::toReadModel);
    }
}
