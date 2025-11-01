package knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.mappers;

import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.challenges.domain.CreatedChallengeResult;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.entity.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChallengeStoreMapper {
    ChallengeState toDomain(ChallengeStateRedis entity);

    CreatedChallengeResult toCreatedResult(ChallengeStateRedis ent);

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

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget ChallengeStateRedis target,
                     FlowStatus status,
                     String reason);
}
