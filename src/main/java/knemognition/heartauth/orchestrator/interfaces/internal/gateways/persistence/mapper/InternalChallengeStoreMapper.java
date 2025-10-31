package knemognition.heartauth.orchestrator.interfaces.internal.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.interfaces.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.interfaces.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;


@Mapper(componentModel = "spring")
public interface InternalChallengeStoreMapper {

    @BeanMapping(qualifiedByName = "createChallenge")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exp", ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "reason", ignore = true)
    @Mapping(target = "modelApiTryId", ignore = true)
    ChallengeStateRedis fromCreate(CreateChallenge src);

    @AfterMapping
    @Named("createChallenge")
    default void fillRest(@MappingTarget ChallengeStateRedis ent,
                          CreateChallenge src
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

    CreatedFlowResult toCreatedResult(ChallengeStateRedis ent);

    ChallengeState toDomain(ChallengeStateRedis ent);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget ChallengeStateRedis target,
                     FlowStatus status,
                     String reason);
}