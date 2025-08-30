package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import org.mapstruct.*;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;

import java.util.UUID;

import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = FlowStatus.class)
public interface ChallengeMapper {

    @Mapping(target = "status",     expression = "java(FlowStatus.PENDING)")
    @Mapping(target = "id",         source = "challengeId")
    @Mapping(target = "userId",     source = "req.userId")
    @Mapping(target = "nonceB64",   source = "nonceB64")
    @Mapping(target = "exp",        source = "exp")
    @Mapping(target = "createdAt",  source = "createdAt")
    @Mapping(target = "reason",     ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    ChallengeState toState(ChallengeCreateRequest req,
                           UUID challengeId,
                           String nonceB64,
                           Long exp,
                           Long createdAt);

    // Simple helper to return the ID we just created
    @Mapping(target = "challengeId", source = "challengeId")
    ChallengeCreateResponse toResponse(UUID challengeId);
}

