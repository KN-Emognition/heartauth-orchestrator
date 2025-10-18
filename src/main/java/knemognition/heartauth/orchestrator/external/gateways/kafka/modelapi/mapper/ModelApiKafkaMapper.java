package knemognition.heartauth.orchestrator.external.gateways.kafka.modelapi.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.EcgPrediction;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelApiKafkaMapper {
    PredictRequestDto toPredictRequestDto(EcgPrediction ecgPrediction);
}

