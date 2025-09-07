package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import org.mapstruct.*;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = FlowStatus.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreateChallengeMapper {


    @Mapping(target = "challengeId", source = "id")
    ChallengeCreateResponse toResponse(CreatedFlowResult result);


    CreateChallenge toCreateChallenge(ChallengeCreateRequest req, String nonceB64);

    @Mapping(target="type", constant = "ECG_CHALLENGE")
    @Mapping(target="nonce", source = "nonceB64")
    @Mapping(target="challengeId",source="res.id")
    MessageData toMessageData(CreatedFlowResult res,String nonceB64);
}

