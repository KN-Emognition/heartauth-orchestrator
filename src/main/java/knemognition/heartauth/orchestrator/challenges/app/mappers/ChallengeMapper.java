package knemognition.heartauth.orchestrator.challenges.app.mappers;

import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.external.interfaces.rest.v1.model.CompleteChallengeRequestDto;
import knemognition.heartauth.orchestrator.security.api.DecryptJweCmd;
import knemognition.heartauth.orchestrator.security.api.ValidateNonceCmd;
import knemognition.heartauth.orchestrator.security.app.mappers.PemMapper;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import knemognition.heartauth.orchestrator.user.api.IdentifiableUserCmd;
import org.mapstruct.*;

import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {PemMapper.class})
public interface ChallengeMapper {
    @BeanMapping(qualifiedByName = "createChallenge")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exp", ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "modelApiTryId", ignore = true)
    ChallengeStateRedis fromCreate(CreateChallengeCmd src);

    @AfterMapping
    @Named("createChallenge")
    default void fillRest(@MappingTarget ChallengeStateRedis ent,
                          CreateChallengeCmd src
    ) {
        ent.setId(UUID.randomUUID());
        ent.setModelApiTryId(UUID.randomUUID());
        long now = Instant.now()
                .getEpochSecond();
        long ttl = src.getTtlSeconds();
        ent.setCreatedAt(now);
        ent.setStatus(FlowStatus.CREATED);
        ent.setTtlSeconds(ttl);
        ent.setExp(now + ttl);
    }

//    CreatedFlowResult toCreatedResult(ChallengeStateRedis ent);

    ChallengeState toDomain(ChallengeStateRedis ent);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget ChallengeStateRedis target,
                     FlowStatus status,
                     String reason);


    @Mapping(target = "nonce", source = "state.nonceB64")
    @Mapping(target = "signature", source = "req.signature")
    @Mapping(target = "pub", source = "state.userPublicKey", qualifiedByName = "pemToEcPublicKey")
    ValidateNonceCmd toValidateNonce(CompleteChallengeRequestDto req, ChallengeState state);

    IdentifiableUserCmd toIdentifiableUser(ChallengeState state);

    @Mapping(target = "recipientPrivateKey", source = "privateKeyPem", qualifiedByName = "pemToEcPrivateKey")
    DecryptJweCmd toDecryptJwe(String jwe, String privateKeyPem, ECPublicKey senderPublicKey);
}
