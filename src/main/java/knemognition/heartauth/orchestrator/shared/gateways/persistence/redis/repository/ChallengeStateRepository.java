package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository;


import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ChallengeStateRepository extends CrudRepository<ChallengeStateRedis, UUID> {
    List<ChallengeStateRedis> findByTenantIdAndUserId(UUID tenantId, UUID userId);
}
