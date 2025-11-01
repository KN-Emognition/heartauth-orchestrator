package knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.repository;


import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.entity.EcgRefDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EcgRefDataRepository extends JpaRepository<EcgRefDataEntity, UUID> {
    Optional<EcgRefDataEntity> findByUserId(UUID userId);
}