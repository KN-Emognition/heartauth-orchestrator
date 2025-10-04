package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalMainStore;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.ExternalMainStoreMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgRefData;
import knemognition.heartauth.orchestrator.shared.app.domain.IdentifiableUser;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.DeviceRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.EcgRefDataRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.TenantRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExternalMainStoreImpl implements ExternalMainStore {

    // mapper
    private final ExternalMainStoreMapper externalMainStoreMapper;
    // repository
    private final EcgRefDataRepository ecgRefDataRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    @Override
    public Optional<EcgRefData> findRefData(IdentifiableUser identUser) {
        return ecgRefDataRepository.findByTenantIdAndUserId(identUser.getTenantId(), identUser.getUserId())
                .map(externalMainStoreMapper::toDomain);
    }

    @Override
    @Transactional
    public void savePairingArtifacts(EcgRefData ref, Device device, IdentifiableUser identUser) {
        // todo: graceful handling, if user already exists or if tenant does not exist
        TenantEntity tenant = tenantRepository.findByTenantId(identUser.getTenantId())
                .orElseThrow();
        UserEntity user = externalMainStoreMapper.toEntity(identUser);
        user.setTenant(tenant);
        user.replaceRefData(externalMainStoreMapper.toEntity(ref));
        user.addDevice(externalMainStoreMapper.toEntity(device));
        userRepository.save(user);
    }
}
