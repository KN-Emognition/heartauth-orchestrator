package knemognition.heartauth.orchestrator.tenants.infrastructure.persistence;

import knemognition.heartauth.orchestrator.tenants.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenants.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenants.domain.Tenant;
import knemognition.heartauth.orchestrator.tenants.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantEntity;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TenantStoreImpl implements TenantStore {
    private final TenantMapper tenantMapper;
    private final TenantRepository tenantRepository;
    private final TenantApiKeyRepository tenantApiKeyRepository;

    @Transactional
    @Override
    public void createTenantWithApiKey(Tenant tenant, TenantApiKey tenantApiKey) {
        TenantEntity tenantEntity = tenantMapper.toEntity(tenant);
        TenantApiKeyEntity key = tenantMapper.toEntity(tenantApiKey);
        tenantEntity.addKey(key);
        tenantRepository.save(tenantEntity);
    }

    @Override
    public Optional<Tenant> get(UUID id) {
        return tenantRepository.findByTenantId(id)
                .map(tenantMapper::toDomain);
    }

    @Override
    public Optional<Tenant> getTenantByApiKey(String keyHash) {
        return tenantApiKeyRepository.findActiveByHash(keyHash)
                .map(tenantApiKey -> tenantMapper.toDomain(tenantApiKey.getTenant()));
    }
}
