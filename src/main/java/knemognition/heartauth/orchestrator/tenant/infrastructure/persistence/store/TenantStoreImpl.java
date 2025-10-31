package knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.store;

import knemognition.heartauth.orchestrator.tenant.app.mappers.TenantMapper;
import knemognition.heartauth.orchestrator.tenant.app.ports.TenantStore;
import knemognition.heartauth.orchestrator.tenant.domain.Tenant;
import knemognition.heartauth.orchestrator.tenant.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.enitity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.enitity.TenantEntity;
import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.repository.TenantRepository;
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
}
