package knemognition.heartauth.orchestrator.modelaction.app.mappers;

import knemognition.heartauth.orchestrator.modelaction.api.ModelActionRead;
import knemognition.heartauth.orchestrator.modelaction.domain.ModelAction;
import knemognition.heartauth.orchestrator.modelaction.infrastructure.persistence.entity.ModelActionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelActionMapper {
    ModelAction toDomain(ModelActionEntity entity);

    ModelActionRead toRead(ModelAction src);
}

