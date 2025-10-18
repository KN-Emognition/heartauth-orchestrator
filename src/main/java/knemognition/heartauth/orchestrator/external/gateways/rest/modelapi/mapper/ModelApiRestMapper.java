package knemognition.heartauth.orchestrator.external.gateways.rest.modelapi.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.EcgPayload;
import knemognition.heartauth.orchestrator.shared.gateways.rest.modelapi.model.PredictRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelApiRestMapper {
    PredictRequestDto toPredictRequestDto(EcgPayload ecgPrediction);
}
