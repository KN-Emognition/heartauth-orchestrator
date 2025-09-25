package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceCredentialEntity;

import java.util.List;
import java.util.UUID;

public interface DeviceCredentialRepository extends JpaRepository<DeviceCredentialEntity, UUID> {

    List<DeviceCredentialEntity> findAllByUserId(UUID userId);

}
