package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository;


import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeStateRepository extends CrudRepository<ChallengeStateRedis, UUID> {
    List<ChallengeStateRedis> findAllByTenantIdAndUserIdOrderByCreatedAtDesc(UUID tenantId, UUID userId);

    Optional<ChallengeStateRedis> findFirstByModelApiTryIdOrderByCreatedAtDesc(UUID correlationId);
}
