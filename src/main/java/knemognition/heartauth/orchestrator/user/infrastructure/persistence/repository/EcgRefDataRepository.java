package knemognition.heartauth.orchestrator.user.infrastructure.persistence.repository;


import knemognition.heartauth.orchestrator.user.infrastructure.persistence.entity.EcgRefDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EcgRefDataRepository extends JpaRepository<EcgRefDataEntity, UUID> {
    @Query("""
                select e from EcgRefDataEntity e
                join e.appUser u
                join u.tenant t
                where t.tenantId = :tenantId and u.userId = :userId
            """)
    Optional<EcgRefDataEntity> findByTenantIdAndUserId(@Param("tenantId") UUID tenantId,
                                                       @Param("userId") UUID userId);
}