package knemognition.heartauth.orchestrator.admin.app.service;

import knemognition.heartauth.orchestrator.admin.app.ports.in.TenantService;
import knemognition.heartauth.orchestrator.admin.app.ports.out.TenantStore;
import knemognition.heartauth.orchestrator.admin.model.CreateTenantResponse;
import knemognition.heartauth.orchestrator.shared.app.ports.in.ApiKeyHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TenantServiceImpl implements TenantService {

    private final TenantStore tenantStore;
    private final ApiKeyHasher apiKeyHasher;

    /**
     * Registers a new tenant with a server-generated externalId and API key.
     * Returns plaintext apiKey once; only its hash is stored.
     */
    @Override
    @Transactional
    public CreateTenantResponse register() {
        UUID externalId = UUID.randomUUID();

        if (tenantStore.findIdByExternalId(externalId).isPresent()) {
            throw new IllegalStateException("externalId collision, retry");
        }

        UUID tenantId = tenantStore.createTenant(externalId);
        log.info("register tenant: {}", tenantId);

        UUID apiKeyPlain = UUID.randomUUID();
        String apiKeyHash = apiKeyHasher.hash(apiKeyPlain.toString());
        tenantStore.storeApiKey(tenantId, apiKeyHash);
        log.info("Stored key hash for tenant {}", tenantId);

        return new CreateTenantResponse()
                .id(tenantId)
                .apiKey(apiKeyPlain);
    }
}
