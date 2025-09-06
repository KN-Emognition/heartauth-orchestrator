package knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;
import java.util.UUID;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreateChallengeStoreMapper {


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "exp", ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    ChallengeStateRedis baseFromCreate(CreateChallenge src, UUID id);

    default ChallengeStateRedis fromCreate(CreateChallenge src, UUID id, long ttlSeconds) {
        long now = Instant.now().getEpochSecond();
        var ent = baseFromCreate(src, id);
        ent.setCreatedAt(now);
        ent.setStatus(FlowStatus.CREATED);
        ent.setTtlSeconds(ttlSeconds);   // @TimeToLive reads this
        ent.setExp(now + ttlSeconds);    // absolute expiry for quick guards
        return ent;
    }


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "exp", source = "exp"),
            @Mapping(target = "ttl", source = "ttlSeconds")
    })
    CreatedFlowResult toCreatedResult(ChallengeStateRedis ent);
}