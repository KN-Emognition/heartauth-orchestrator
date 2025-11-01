package knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.repository;


import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.entity.ChallengeStateRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeStateRepository extends CrudRepository<ChallengeStateRedis, UUID> {
    List<ChallengeStateRedis> findAllByTenantIdAndUserIdOrderByCreatedAtDesc(UUID tenantId, UUID userId);

    Optional<ChallengeStateRedis> findFirstByModelApiTryIdOrderByCreatedAtDesc(UUID correlationId);
}
