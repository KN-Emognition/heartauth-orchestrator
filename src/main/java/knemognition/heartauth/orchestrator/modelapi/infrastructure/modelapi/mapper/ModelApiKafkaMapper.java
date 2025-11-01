package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.mapper;

import knemognition.heartauth.orchestrator.modelapi.api.CompleteChallengeWithPredictionResultCmd;
import knemognition.heartauth.orchestrator.modelapi.api.EcgSendPredictCmd;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictRequestDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictResponseDto;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ModelApiKafkaMapper {
    PredictRequestDto toPredictRequestDto(EcgSendPredictCmd ecgPrediction);

    CompleteChallengeWithPredictionResultCmd toCmd(PredictResponseDto src, UUID correlationId);
}

