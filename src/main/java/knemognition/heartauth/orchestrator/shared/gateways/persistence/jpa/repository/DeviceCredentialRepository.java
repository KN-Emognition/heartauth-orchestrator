package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;


import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.DeviceCredentialEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DeviceCredentialRepository extends JpaRepository<DeviceCredentialEntity, UUID> {


    @Query("""
            select distinct d.fcmToken
            from DeviceCredentialEntity d
            where d.userId = :userId
              and d.revokedAt is null
              and d.fcmToken is not null
              and length(trim(d.fcmToken)) > 0
            """)
    List<String> findActiveFcmTokensByUser(@Param("userId") UUID userId);

}
