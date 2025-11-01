package knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.repository;


import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.entity.ModelActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelActionRepository extends JpaRepository<ModelActionEntity, UUID> {
}
