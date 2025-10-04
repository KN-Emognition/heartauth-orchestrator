package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository;

import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PairingStateRepository extends CrudRepository<PairingStateRedis, UUID> {
}
