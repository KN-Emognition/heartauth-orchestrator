package knemognition.heartauth.orchestrator.admin.app.impl;

import knemognition.heartauth.orchestrator.admin.app.ports.in.TenantService;
import knemognition.heartauth.orchestrator.admin.app.ports.out.TenantStore;
import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.shared.app.ports.in.ApiKeyHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public CreateTenantResponseDto register() {
        UUID externalId = UUID.randomUUID();

        if (tenantStore.findIdByExternalId(externalId)
                .isPresent()) {
            throw new IllegalStateException("externalId collision, retry");
        }

        UUID tenantId = tenantStore.createTenant(externalId);
        log.info("register tenant: {}", tenantId);

        UUID apiKeyPlain = UUID.randomUUID();
        String apiKeyHash = apiKeyHasher.hash(apiKeyPlain.toString());
        tenantStore.storeApiKeyByExternalId(tenantId, apiKeyHash);
        log.info("Stored key hash for tenant {}", tenantId);

        return new CreateTenantResponseDto()
                .id(tenantId)
                .apiKey(apiKeyPlain);
    }
}
