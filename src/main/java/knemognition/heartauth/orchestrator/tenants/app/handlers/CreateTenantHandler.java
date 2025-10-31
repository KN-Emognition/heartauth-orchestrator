package knemognition.heartauth.orchestrator.tenants.app.handlers;

import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import knemognition.heartauth.orchestrator.tenants.domain.TenantApiKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateTenantHandler {
    private final SecurityModule securityModule;
    private final TenantStore tenantStore;

    public CreatedTenant handle() {
        log.info("[TENANT] Creating new tenant");
        UUID tenantId = UUID.randomUUID();
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .build();

        UUID apiKey = UUID.randomUUID();
        String apiKeyHash = securityModule.hash(apiKey
                .toString());

        TenantApiKey tenantApiKey = TenantApiKey.builder()
                .keyHash(apiKeyHash)
                .build();
        tenantStore.createTenantWithApiKey(tenant, tenantApiKey);
        log.info("[TENANT] Stored tenant with apiKey");

        return CreatedTenant.builder()
                .tenantId(tenantId)
                .apiKey(apiKey)
                .build();
    }
}
