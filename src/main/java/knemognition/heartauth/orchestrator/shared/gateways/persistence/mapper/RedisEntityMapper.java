package knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RedisEntityMapper {
    PairingState toDomain(PairingStateRedis entity);

    ChallengeState toDomain(ChallengeStateRedis entity);
}
