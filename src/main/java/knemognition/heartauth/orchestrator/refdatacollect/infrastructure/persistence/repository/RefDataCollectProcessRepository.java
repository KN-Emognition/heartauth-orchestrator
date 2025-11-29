package knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.repository;

import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity.RefDataCollectProcessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefDataCollectProcessRepository extends JpaRepository<RefDataCollectProcessEntity, UUID> {
    Optional<RefDataCollectProcessEntity> findByProcessId(UUID processId);
}
