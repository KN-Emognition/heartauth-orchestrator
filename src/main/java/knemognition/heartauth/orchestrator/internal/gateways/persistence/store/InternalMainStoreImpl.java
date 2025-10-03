package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import jakarta.transaction.Transactional;
import knemognition.heartauth.orchestrator.internal.app.domain.IdentifyUser;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalMainStore;
import knemognition.heartauth.orchestrator.shared.app.domain.Device;
import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.DeviceRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.TenantApiKeyRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.UserRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.DeviceMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.TenantApiKeyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InternalMainStoreImpl implements InternalMainStore {

    // mappers
    private final TenantApiKeyMapper tenantApiKeyMapper;
    private final DeviceMapper deviceMapper;
    // repositories
    private final TenantApiKeyRepository tenantApiKeyRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public boolean checkIfUserExists(IdentifyUser data) {
        return userRepository.existsByTenantExternalIdAndUserId(data.getTenantId(), data.getUserId());
    }

    @Override
    public Optional<TenantApiKey> findActiveByHash(String keyHash) {
        return tenantApiKeyRepository.findActiveByHash(keyHash)
                .map(tenantApiKeyMapper::toDomain);
    }

    @Override
    @Transactional
    public void updateLastUsedAt(UUID apiKeyId, OffsetDateTime when) {
        TenantApiKeyEntity key = tenantApiKeyRepository.getReferenceById(apiKeyId);
        key.setLastUsedAt(when);
        tenantApiKeyRepository.save(key);
    }

    @Override
    public List<Device> findDevices(
            IdentifyUser data) {
        return deviceRepository.findAllByTenantExternalIdAndUserId(data.getTenantId(), data.getUserId())
                .stream()
                .map(deviceMapper::toDomain)
                .toList();
    }
}
