package knemognition.heartauth.orchestrator.admin.app.mapper;

import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.ModelActionDto;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ModelActionMapper {
    ModelActionDto toDto(UUID id, CombinedModelActionDto src);

}

