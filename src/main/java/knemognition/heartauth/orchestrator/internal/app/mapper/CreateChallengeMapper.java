package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.domain.MessageData;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.security.PrivateKey;
import java.security.PublicKey;

@Mapper(componentModel = "spring", imports = {FlowStatus.class, KeyLoader.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreateChallengeMapper {


    @Mapping(target = "challengeId", source = "id")
    ChallengeCreateResponse toResponse(CreatedFlowResult result);

    @Mapping(target = "ttlSeconds", source = "effectiveTtl")
    @Mapping(target = "privateKey", expression = "java(KeyLoader.toPem(privateKey,\"PRIVATE\"))")
    @Mapping(target = "userPublicKey", source = "publicKeyPem")
    CreateChallenge toCreateChallenge(ChallengeCreateRequest req, String nonceB64, Integer effectiveTtl, PrivateKey privateKey, String publicKeyPem);

    @Mapping(target = "type", constant = "ECG_CHALLENGE")
    @Mapping(target = "nonce", source = "nonceB64")
    @Mapping(target = "challengeId", source = "res.id")
    @Mapping(target = "publicKey", expression = "java(KeyLoader.toPem(publicKey,\"PUBLIC\"))")
    MessageData toMessageData(CreatedFlowResult res, String nonceB64, PublicKey publicKey);
}

