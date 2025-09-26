package knemognition.heartauth.orchestrator.external.gateways.persistence.store;


import knemognition.heartauth.orchestrator.shared.app.domain.DeviceCredential;
import knemognition.heartauth.orchestrator.external.app.ports.out.CreateDeviceCredentialStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceCredentialEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.DeviceCredentialMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.DeviceCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateDeviceCredentialStoreImpl implements CreateDeviceCredentialStore {

    private final DeviceCredentialMapper mapper;
    private final DeviceCredentialRepository deviceCredentialRepository;

    @Override
    @Transactional
    public void create(DeviceCredential toCreate) {
        DeviceCredentialEntity saved = deviceCredentialRepository.save(mapper.toEntity(toCreate));
        mapper.toDomain(saved);
    }

}
