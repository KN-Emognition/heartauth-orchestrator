package knemognition.heartauth.orchestrator.admin.app.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.EcgPayload;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgPrediction;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelActionMapper {
    EcgPayload toDomain(PredictRequestDto src);

    EcgPrediction toDomain(PredictResponseDto src);

}

