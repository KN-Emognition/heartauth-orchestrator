package knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository;


import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.ModelActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelActionRepository extends JpaRepository<ModelActionEntity, UUID> {
}
