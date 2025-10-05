package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;

import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("""
              select (count(u) > 0) from UserEntity u
              join u.tenant t
              where t.tenantId = :tenantExternalId and u.userId = :userId
            """)
    boolean existsByTenantIdAndUserId(@Param("tenantExternalId") UUID tenantId,
                                      @Param("userId") UUID userId);
}