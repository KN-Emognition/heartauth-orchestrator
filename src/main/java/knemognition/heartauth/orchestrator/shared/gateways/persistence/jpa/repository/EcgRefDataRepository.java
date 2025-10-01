package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;


import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.EcgRefDataEntity;
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
                where t.externalId = :tenantExternalId and u.userId = :userId
            """)
    Optional<EcgRefDataEntity> findByTenantExternalIdAndUserId(@Param("tenantExternalId") UUID tenantExternalId,
                                                               @Param("userId") UUID userId);
}