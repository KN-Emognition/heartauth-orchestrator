package knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.mappers;

import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.entity.EcgRefDataEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EcgRefMapper {

    RefEcg toDomain(EcgRefDataEntity ecgRefDataEntity);
}
