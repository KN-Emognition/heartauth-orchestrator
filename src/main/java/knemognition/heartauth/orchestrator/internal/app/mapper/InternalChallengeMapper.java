package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreateChallengeRequestDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreateChallengeResponseDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.FlowStatusDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengePushMessage;
import knemognition.heartauth.orchestrator.shared.app.domain.MessageType;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {KeyLoader.class, MessageType.class})
public interface InternalChallengeMapper {


    @Mapping(target = "challengeId", source = "id")
    CreateChallengeResponseDto toCreateChallengeResponse(CreatedFlowResult result);


    @Mapping(target = "ephemeralPrivateKey", expression = "java(KeyLoader.toPem(privateKey,\"PRIVATE KEY\"))")
    CreateChallenge toCreateChallenge(UUID tenantId, CreateChallengeRequestDto req, String nonceB64, Integer ttlSeconds, PrivateKey privateKey, String userPublicKey);


    default StatusResponseDto notFoundStatus() {
        var r = new StatusResponseDto();
        r.setStatus(FlowStatusDto.NOT_FOUND);
        return r;
    }

    @Mapping(target = "type", expression = "java(MessageType.CHALLENGE)")
    @Mapping(target = "challengeId", source = "res.id")
    @Mapping(target = "ttl", source = "res.ttlSeconds")
    @Mapping(target = "publicKey", expression = "java(KeyLoader.toPem(publicKey,\"PUBLIC KEY\"))")
    ChallengePushMessage toChallengePushMessage(CreatedFlowResult res, String nonce, PublicKey publicKey);
}

