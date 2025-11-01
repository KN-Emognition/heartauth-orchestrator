package knemognition.heartauth.orchestrator.api;

import knemognition.heartauth.orchestrator.api.rest.v1.admin.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.api.rest.v1.external.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.external.model.CompletePairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.external.model.InitPairingRequestDto;
import knemognition.heartauth.orchestrator.api.rest.v1.external.model.InitPairingResponseDto;
import knemognition.heartauth.orchestrator.api.rest.v1.internal.model.*;
import knemognition.heartauth.orchestrator.challenges.api.*;
import knemognition.heartauth.orchestrator.pairings.api.*;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import knemognition.heartauth.orchestrator.tenants.api.CreatedTenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DtoMapper {
    StatusResponseDto toDto(PairingStatusRead src);

    StatusResponseDto toDto(ChallengeStatusRead src);

    @Mapping(target = "id", source = "tenantId")
    CreateTenantResponseDto toDto(CreatedTenant src);

    CreatePairingCmd toCmd(CreatePairingRequestDto src, UUID tenantId);

    CreatePairingResponseDto toDto(CreatedPairingRead src);

    CreateChallengeCmd toCmd(CreateChallengeRequestDto src, UUID tenantId);

    CreateChallengeResponseDto toDto(CreatedChallengeRead src);

    CompleteChallengeWithPredictionResultCmd toCmd(PredictResponseDto src, UUID correlationId);

    CompleteChallengeWithPredictionPayloadCmd toCmd(CompleteChallengeRequestDto src, UUID challengeId);

    InitPairingCmd toCmd(InitPairingRequestDto src);

    InitPairingResponseDto toDto(InitPairingRead src);

    CompletePairingCmd toCmd(CompletePairingRequestDto src);

}
