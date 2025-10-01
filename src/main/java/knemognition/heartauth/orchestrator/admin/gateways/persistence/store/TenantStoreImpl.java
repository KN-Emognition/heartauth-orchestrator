package knemognition.heartauth.orchestrator.admin.gateways.persistence.store;

import knemognition.heartauth.orchestrator.admin.app.ports.out.TenantStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenantStoreImpl implements TenantStore {

    private final TenantRepository tenantRepository;
    private final TenantApiKeyRepository apiKeyRepository;

    @Override
    public Optional<UUID> findIdByExternalId(UUID externalId) {
        return tenantRepository.findByExternalId(externalId).map(TenantEntity::getId);
    }

    @Override
    @Transactional
    public UUID createTenant(UUID externalId) {
        TenantEntity saved = tenantRepository.save(
                TenantEntity.builder().externalId(externalId).build()
        );
        return saved.getId();
    }

    @Override
    @Transactional
    public void storeApiKey(UUID tenantId, String keyHash) {
        TenantEntity tenantRef = tenantRepository.getReferenceById(tenantId);

        TenantApiKeyEntity key = TenantApiKeyEntity.builder()
                .tenant(tenantRef)
                .keyHash(keyHash)
                .build();

        apiKeyRepository.save(key);
    }
}
