package knemognition.heartauth.orchestrator.shared.gateways.persistence.store;

import knemognition.heartauth.orchestrator.shared.app.ports.out.DeviceCredentialStore;
import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.DeviceCredentialRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.DeviceCredentialMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceCredentialStoreImpl implements DeviceCredentialStore {

    private final DeviceCredentialRepository deviceCredentialRepository;
    private final DeviceCredentialMapper deviceCredentialMapper;


    @Override
    public List<DeviceCredential> getDeviceCredentials(UUID userId) {
        return deviceCredentialRepository.findAllByUserId(userId)
                .stream()
                .map(deviceCredentialMapper::toDomain)
                .toList();
    }
}