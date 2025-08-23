package knemognition.heartauth.orchestrator.internal.gateways.persistence.jpa.repository;


import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.jpa.entity.DeviceCredentialEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DeviceCredentialRepository extends JpaRepository<DeviceCredentialEntity, UUID> {

    @Query("""
       select distinct e.fcmToken
       from DeviceCredentialEntity e
       where e.userId = :userId
         and e.revokedAt is null
         and e.fcmToken is not null
    """)
    List<String> findActiveFcmTokensByUser(@Param("userId") String userId);
}
