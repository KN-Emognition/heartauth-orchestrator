package knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
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
    ChallengeStateRedis fromCreate(CreateChallenge src);

    @AfterMapping
    @Named("createChallenge")
    default void fillRest(@MappingTarget ChallengeStateRedis ent,
                          CreateChallenge src
    ) {
        ent.setId(UUID.randomUUID());
        long now = Instant.now()
                .getEpochSecond();
        long ttl = src.getTtlSeconds();
        ent.setCreatedAt(now);
        ent.setStatus(FlowStatus.CREATED);
        ent.setTtlSeconds(ttl);
        ent.setExp(now + ttl);
    }

    CreatedFlowResult toCreatedResult(ChallengeStateRedis ent);


}