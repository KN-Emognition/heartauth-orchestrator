package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository;


import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChallengeStateRepository extends CrudRepository<ChallengeStateRedis, UUID> {
    Optional<ChallengeStateRedis> findTopByTenantIdAndUserIdOrderByCreatedAtDesc(UUID tenantId, UUID userId);
}
