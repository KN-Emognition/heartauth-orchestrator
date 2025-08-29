package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;


import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceCredentialEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


import org.springframework.data.jpa.repository.*;

import java.time.Instant;
import java.util.*;

public interface DeviceCredentialRepository extends JpaRepository<DeviceCredentialEntity, UUID> {

    long countByUserIdAndRevokedAtIsNull(UUID userId);

    Optional<DeviceCredentialEntity> findByDeviceIdAndRevokedAtIsNull(String deviceId);

    @Query("""
           select distinct d.fcmToken
           from DeviceCredentialEntity d
           where d.userId = :userId
             and d.revokedAt is null
             and d.fcmToken is not null
             and length(trim(d.fcmToken)) > 0
           """)
    List<String> findActiveFcmTokensByUser(@Param("userId") UUID userId);

    List<DeviceCredentialEntity> findByUserIdAndRevokedAtIsNullOrderByCreatedAtDesc(UUID userId);

    @Modifying
    @Query("update DeviceCredentialEntity d set d.lastSeenAt = :at where d.id = :id and d.revokedAt is null")
    int touchLastSeen(@Param("id") UUID id, @Param("at") Instant at);

    @Modifying
    @Query("update DeviceCredentialEntity d set d.fcmToken = :tok where d.id = :id and d.revokedAt is null")
    int updateFcmToken(@Param("id") UUID id, @Param("tok") String token);

    @Modifying
    @Query("update DeviceCredentialEntity d set d.revokedAt = :at where d.id = :id and d.revokedAt is null")
    int revokeActive(@Param("id") UUID id, @Param("at") Instant at);
}
