package knemognition.heartauth.orchestrator.users.infastructure.persistence.repository;

import knemognition.heartauth.orchestrator.users.infastructure.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {

    List<DeviceEntity> findAllByAppUser_UserIdAndAppUser_TenantId(UUID userId, UUID tenantId);

}