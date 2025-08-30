package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.store;


import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.app.ports.out.DeviceCredentialStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceCredentialEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.mapper.DeviceCredentialMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.DeviceCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceCredentialStoreImpl implements DeviceCredentialStore {

    private final DeviceCredentialRepository repo;
    private final DeviceCredentialMapper mapper;

    @Override
    @Transactional
    public DeviceCredential create(DeviceCredential toCreate) {
        try {
            DeviceCredentialEntity saved = repo.save(mapper.toEntity(toCreate));
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            // surface DB partial unique violations (device_id active / (user_id, device_id) active)
            throw ex;
        }
    }

    @Override
    public Optional<DeviceCredential> findActiveByDeviceId(String deviceId) {
        return repo.findByDeviceIdAndRevokedAtIsNull(deviceId).map(mapper::toDomain);
    }

    @Override
    public List<DeviceCredential> listActiveByUserId(UUID userId) {
        return repo.findByUserIdAndRevokedAtIsNullOrderByCreatedAtDesc(userId)
                .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public long countActiveByUserId(UUID userId) {
        return repo.countByUserIdAndRevokedAtIsNull(userId);
    }

    @Override
    @Transactional
    public void updateFcmToken(UUID id, String newFcmToken) {
        repo.updateFcmToken(id, newFcmToken);
    }

    @Override
    @Transactional
    public void touchLastSeen(UUID id, Instant at) {
        repo.touchLastSeen(id, at);
    }

    @Override
    @Transactional
    public boolean revoke(UUID id, Instant at) {
        return repo.revokeActive(id, at) > 0;
    }

    @Override
    public Optional<DeviceCredential> findById(UUID id) {
        return repo.findById(id).map(mapper::toDomain);
    }
}
