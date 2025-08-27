package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ChallengeStateRedisMapper {

    @Mapping(target = "ttlSeconds", ignore = true)
    ChallengeStateRedis toRedis(ChallengeState src, @Context Long ttlSeconds);

    @Mapping(target = "ttlSeconds", ignore = true)
    ChallengeState toDomain(ChallengeStateRedis src, @Context Long overrideTtlSeconds);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget ChallengeStateRedis target,
                     knemognition.heartauth.orchestrator.external.model.FlowStatus status,
                     String reason);

    @AfterMapping
    default void afterToRedis(ChallengeState src, @MappingTarget ChallengeStateRedis tgt, @Context Long ttlSeconds) {
        if (ttlSeconds != null) tgt.setTtlSeconds(ttlSeconds);
    }

    @AfterMapping
    default void afterToDomain(ChallengeStateRedis src, @MappingTarget ChallengeState tgt, @Context Long overrideTtlSeconds) {
        tgt.setTtlSeconds(overrideTtlSeconds != null ? overrideTtlSeconds : src.getTtlSeconds());
    }
}
