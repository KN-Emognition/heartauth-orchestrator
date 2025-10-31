package knemognition.heartauth.orchestrator.pairing.redis.repository;

import knemognition.heartauth.orchestrator.pairing.redis.model.PairingStateRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface PairingStateRepository extends CrudRepository<PairingStateRedis, UUID> {
    List<PairingStateRedis> findAllByTenantIdAndUserIdOrderByCreatedAtDesc(UUID tenantId, UUID userId);
}
