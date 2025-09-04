package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChallengeStateRedisMapper {


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "exp", ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    ChallengeStateRedis baseFromCreate(CreateChallenge src, UUID id);

    // Our default method sets the dynamic fields in seconds.
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

    FlowStatusDescription toStatus(ChallengeStateRedis src);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget ChallengeStateRedis target,
                     knemognition.heartauth.orchestrator.internal.model.FlowStatus status,
                     String reason);


}
