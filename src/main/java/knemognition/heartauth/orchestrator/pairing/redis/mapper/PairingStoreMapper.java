package knemognition.heartauth.orchestrator.pairing.redis.mapper;

import knemognition.heartauth.orchestrator.pairing.redis.model.PairingStateRedis;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PairingStoreMapper {
    PairingState toDomain(PairingStateRedis entity);

}
