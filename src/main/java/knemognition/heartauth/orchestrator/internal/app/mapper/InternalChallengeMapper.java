package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.domain.SendPushMessage;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.ChallengeCreateResponse;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.utils.KeyLoader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {FlowStatus.class, KeyLoader.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InternalChallengeMapper {


    @Mapping(target = "challengeId", source = "id")
    ChallengeCreateResponse toCreateChallengeResponse(CreatedFlowResult result);


    @Mapping(target = "ephemeralPrivateKey", expression = "java(KeyLoader.toPem(privateKey,\"PRIVATE KEY\"))")
    CreateChallenge toCreateChallenge(UUID tenantId, ChallengeCreateRequest req, String nonceB64, Integer ttlSeconds, PrivateKey privateKey, String userPublicKey);


    StatusResponse map(FlowStatusDescription description);

    default StatusResponse notFoundStatus() {
        var r = new StatusResponse();
        r.setStatus(FlowStatus.NOT_FOUND);
        return r;
    }

    @Mapping(target = "type", constant = "ECG_CHALLENGE")
    @Mapping(target = "challengeId", source = "res.id")
    @Mapping(target="ttl", source = "res.ttlSeconds")
    @Mapping(target = "publicKey", expression = "java(KeyLoader.toPem(publicKey,\"PUBLIC KEY\"))")
    SendPushMessage toMessageData(CreatedFlowResult res, String nonce, PublicKey publicKey);
}

