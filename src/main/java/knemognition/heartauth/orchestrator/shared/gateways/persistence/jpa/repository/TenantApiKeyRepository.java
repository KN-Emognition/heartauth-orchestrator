package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;

import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantApiKeyRepository extends JpaRepository<TenantApiKeyEntity, UUID> {
    @Query("""
                select k from TenantApiKeyEntity k
                join k.tenant t
                where t.tenantId = :tenantId and k.keyHash = :keyHash
            """)
    Optional<TenantApiKeyEntity> findByTenantExternalIdAndKeyHash(@Param("tenantId") UUID tenantId,
                                                                  @Param("keyHash") String keyHash);

    List<TenantApiKeyEntity> findAllByTenant_Id(UUID tenantId);


    @Query("""
        select k from TenantApiKeyEntity k
        join fetch k.tenant t
        where k.keyHash = :hash
          and (k.revokedAt is null)
          and (k.expiresAt is null or k.expiresAt > CURRENT_TIMESTAMP)
    """)
    Optional<TenantApiKeyEntity> findActiveByHash(@Param("hash") String hash);
}