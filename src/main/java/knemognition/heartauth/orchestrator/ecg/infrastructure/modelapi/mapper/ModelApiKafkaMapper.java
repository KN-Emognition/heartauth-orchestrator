package knemognition.heartauth.orchestrator.ecg.infrastructure.modelapi.mapper;

import knemognition.heartauth.orchestrator.ecg.domain.EcgPayload;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelApiKafkaMapper {
    PredictRequestDto toPredictRequestDto(EcgPayload ecgPrediction);
}

