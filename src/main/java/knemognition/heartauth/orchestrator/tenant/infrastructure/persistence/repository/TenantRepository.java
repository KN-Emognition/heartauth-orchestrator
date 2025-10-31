package knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.repository;

import knemognition.heartauth.orchestrator.tenant.infrastructure.persistence.enitity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
    boolean existsByTenantId(UUID tenantId);

    Optional<TenantEntity> findByTenantId(UUID ternantId);
}
