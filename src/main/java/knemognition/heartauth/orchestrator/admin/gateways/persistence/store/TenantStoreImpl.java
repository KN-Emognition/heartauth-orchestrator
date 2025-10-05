package knemognition.heartauth.orchestrator.admin.gateways.persistence.store;

import knemognition.heartauth.orchestrator.admin.app.ports.out.TenantStore;
import knemognition.heartauth.orchestrator.shared.app.domain.Tenant;
import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.TenantRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.MainStoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TenantStoreImpl implements TenantStore {

    private final MainStoreMapper mainStoreMapper;
    // repository
    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public void createTenantWithApiKey(Tenant tenant, TenantApiKey tenantApiKey) {
        // todo: graceful handling
        TenantEntity tenantEntity = mainStoreMapper.toEntity(tenant);
        TenantApiKeyEntity key = mainStoreMapper.toEntity(tenantApiKey);
        tenantEntity.addKey(key);
        tenantRepository.save(tenantEntity);
    }
}
