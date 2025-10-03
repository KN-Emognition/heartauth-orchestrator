package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.domain.SendPushMessage;
import knemognition.heartauth.orchestrator.internal.model.CreateChallengeRequestDto;
import knemognition.heartauth.orchestrator.internal.model.CreateChallengeResponseDto;
import knemognition.heartauth.orchestrator.internal.model.FlowStatusDto;
import knemognition.heartauth.orchestrator.internal.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {KeyLoader.class})
public interface InternalChallengeMapper {


    @Mapping(target = "challengeId", source = "id")
    CreateChallengeResponseDto toCreateChallengeResponse(CreatedFlowResult result);


    @Mapping(target = "ephemeralPrivateKey", expression = "java(KeyLoader.toPem(privateKey,\"PRIVATE KEY\"))")
    CreateChallenge toCreateChallenge(UUID tenantId, CreateChallengeRequestDto req, String nonceB64, Integer ttlSeconds, PrivateKey privateKey, String userPublicKey);


    StatusResponseDto map(FlowStatusDescription description);

    default StatusResponseDto notFoundStatus() {
        var r = new StatusResponseDto();
        r.setStatus(FlowStatusDto.NOT_FOUND);
        return r;
    }

    @Mapping(target = "type", constant = "ECG_CHALLENGE")
    @Mapping(target = "challengeId", source = "res.id")
    @Mapping(target = "ttl", source = "res.ttlSeconds")
    @Mapping(target = "publicKey", expression = "java(KeyLoader.toPem(publicKey,\"PUBLIC KEY\"))")
    SendPushMessage toMessageData(CreatedFlowResult res, String nonce, PublicKey publicKey);
}

