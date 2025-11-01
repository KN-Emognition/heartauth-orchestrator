package knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.repository;

import knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.entity.PairingStateRedis;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@EnableRedisRepositories
public interface PairingStateRepository extends CrudRepository<PairingStateRedis, UUID> {
    List<PairingStateRedis> findAllByTenantIdAndUserIdOrderByCreatedAtDesc(UUID tenantId, UUID userId);
}
