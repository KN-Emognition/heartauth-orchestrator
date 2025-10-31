package knemognition.heartauth.orchestrator.user.infrastructure.persistence.repository;

import knemognition.heartauth.orchestrator.user.infrastructure.persistence.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
    @Query("""
                select d from DeviceEntity d
                join d.appUser u
                where u.tenantId = :tenantId and u.userId = :userId
                order by d.createdAt asc
            """)
    List<DeviceEntity> findAllByTenantIdAndUserId(@Param("tenantId") UUID tenantId,
                                                  @Param("userId") UUID userId);
}