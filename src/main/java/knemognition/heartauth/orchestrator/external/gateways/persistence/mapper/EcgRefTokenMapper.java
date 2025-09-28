package knemognition.heartauth.orchestrator.external.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EcgRefToken;
import knemognition.heartauth.orchestrator.external.gateways.persistence.jpa.entity.EcgRefTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EcgRefTokenMapper {
    EcgRefToken toDomain(EcgRefTokenEntity e);

    EcgRefTokenEntity toEntity(EcgRefToken d);
}
