package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import org.mapstruct.*;
import knemognition.heartauth.orchestrator.internal.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    @Mappings({
            @Mapping(target = "state", constant = "PENDING"),
            @Mapping(target = "challengeId", source = "challengeId"),
            @Mapping(target = "userId", source = "req.userId"),
            @Mapping(target = "nonceB64", source = "nonceB64"),
            @Mapping(target = "exp", source = "exp"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "reason", ignore = true)
    })
    ChallengeState toState(ChallengeCreateRequest req,
                           UUID challengeId,
                           String nonceB64,
                           Long exp,
                           Long createdAt);

    @Mapping(target = "challengeId", source = "challengeId")
    ChallengeCreateResponse toResponse(UUID challengeId);


}
