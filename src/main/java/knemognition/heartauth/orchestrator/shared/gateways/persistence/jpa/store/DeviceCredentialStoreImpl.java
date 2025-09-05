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

    private final DeviceCredentialMapper mapper;
    private final DeviceCredentialRepository deviceCredentialRepository;

    @Override
    @Transactional
    public DeviceCredential create(DeviceCredential toCreate) {
        try {
            DeviceCredentialEntity saved = deviceCredentialRepository.save(mapper.toEntity(toCreate));
            return mapper.toDomain(saved);
        } catch (DataIntegrityViolationException ex) {
            // surface DB partial unique violations (device_id active / (user_id, device_id) active)
            throw ex;
        }
    }

    @Override
    public List<String> getActiveFcmTokens(UUID userId) {
        return deviceCredentialRepository.findActiveFcmTokensByUser(userId);
    }
}
