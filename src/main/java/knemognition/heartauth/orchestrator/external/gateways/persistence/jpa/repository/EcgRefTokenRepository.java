package knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.repository;


import knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.entity.EcgRefTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EcgRefTokenRepository extends JpaRepository<EcgRefTokenEntity, UUID> {

    Optional<List<EcgRefTokenEntity>> findAllByUserId(UUID userId);

}