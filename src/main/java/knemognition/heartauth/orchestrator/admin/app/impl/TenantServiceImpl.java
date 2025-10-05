package knemognition.heartauth.orchestrator.admin.app.impl;

import knemognition.heartauth.orchestrator.admin.app.ports.in.TenantService;
import knemognition.heartauth.orchestrator.admin.app.ports.out.TenantStore;
import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.Tenant;
import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.shared.app.ports.in.ApiKeyHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 *  {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {

    private final TenantStore tenantStore;
    private final ApiKeyHasher apiKeyHasher;

    /**
     *  {@inheritDoc}
     */
    @Override
    public CreateTenantResponseDto register() {
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
        log.info("Stored tenant with apiKey");

        return new CreateTenantResponseDto()
                .id(tenantId)
                .apiKey(apiKey);
    }
}
