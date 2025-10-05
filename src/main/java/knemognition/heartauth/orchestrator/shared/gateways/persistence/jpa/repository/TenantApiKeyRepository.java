package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;

import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.TenantApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TenantApiKeyRepository extends JpaRepository<TenantApiKeyEntity, UUID> {
    @Query("""
              select k from TenantApiKeyEntity k
                join fetch k.tenant t
              where k.keyHash = :hash 
            """)
    Optional<TenantApiKeyEntity> findActiveByHash(@Param("hash") String hash);
}