package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;

import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("""
                select u from UserEntity u
                join fetch u.tenant t
                where t.externalId = :tenantExternalId and u.userId = :userId
            """)
    Optional<UserEntity> findByTenantExternalIdAndUserId(@Param("tenantExternalId") UUID tenantExternalId,
                                                         @Param("userId") UUID userId);

    List<UserEntity> findAllByTenant_Id(UUID tenantId);
}