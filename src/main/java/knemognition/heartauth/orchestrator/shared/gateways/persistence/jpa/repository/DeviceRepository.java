package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;

import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    @Query("""
                select d from DeviceEntity d
                join d.appUser u
                join u.tenant t
                where t.externalId = :tenantExternalId and u.userId = :userId and d.deviceId = :deviceId
            """)
    Optional<DeviceEntity> findByTenantExternalIdAndUserIdAndDeviceId(@Param("tenantExternalId") UUID tenantExternalId,
                                                                      @Param("userId") UUID userId,
                                                                      @Param("deviceId") String deviceId);

    List<DeviceEntity> findAllByAppUser_Id(UUID appUserId);

    @Query("""
                select d from DeviceEntity d
                where d.appUser = :user and d.revokedAt is null
            """)
    List<DeviceEntity> findActiveByUser(@Param("user") UserEntity user);
}