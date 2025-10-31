package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalMainStore;
import knemognition.heartauth.orchestrator.user.domain.Device;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.repository.DeviceRepository;
import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.user.infrastructure.persistence.repository.UserRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.MainStoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InternalMainStoreImpl implements InternalMainStore {

    // mappers
    private final MainStoreMapper mainStoreMapper;
    // repositories
    private final TenantApiKeyRepository tenantApiKeyRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfUserExists(IdentifiableUserCmd data) {
        return userRepository.existsByTenantIdAndUserId(data.getTenantId(), data.getUserId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UUID> getTenantIdForActiveKeyHash(String keyHash) {
        return tenantApiKeyRepository.findActiveByHash(keyHash)
                .map(apiKey -> apiKey.getTenant()
                        .getTenantId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Device> findDevices(
            IdentifiableUserCmd data) {
        return deviceRepository.findAllByTenantIdAndUserId(data.getTenantId(), data.getUserId())
                .stream()
                .map(mainStoreMapper::toDomain)
                .toList();
    }
}
