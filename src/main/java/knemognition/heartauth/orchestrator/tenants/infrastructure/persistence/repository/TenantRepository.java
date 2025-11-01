package knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.repository;

import knemognition.heartauth.orchestrator.tenants.infrastructure.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
    boolean existsByTenantId(UUID tenantId);

    Optional<TenantEntity> findByTenantId(UUID tenantId);
}
