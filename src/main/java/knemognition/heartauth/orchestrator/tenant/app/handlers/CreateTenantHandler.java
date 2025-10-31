package knemognition.heartauth.orchestrator.tenant.app.handlers;

import knemognition.heartauth.orchestrator.tenant.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenant.app.ports.ApiKeyHasher;
import knemognition.heartauth.orchestrator.tenant.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenant.domain.Tenant;
import knemognition.heartauth.orchestrator.tenant.domain.TenantApiKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateTenantHandler {
    private final ApiKeyHasher apiKeyHasher;
    private final TenantStore tenantStore;

    public CreatedTenant handle() {
        UUID tenantId = UUID.randomUUID();
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .build();

        UUID apiKey = UUID.randomUUID();
        String apiKeyHash = apiKeyHasher.hash(apiKey
                .toString());

        TenantApiKey tenantApiKey = TenantApiKey.builder()
                .keyHash(apiKeyHash)
                .build();
        tenantStore.createTenantWithApiKey(tenant, tenantApiKey);
        log.info("[TENANT] Stored tenant with apiKey");

        return CreatedTenant.builder()
                .id(tenantId)
                .apiKey(apiKey)
                .build();
    }
}
