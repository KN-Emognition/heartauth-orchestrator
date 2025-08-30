package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper;

import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PairingStateRedisMapper {

    @Mapping(target = "ttlSeconds", ignore = true)
    PairingStateRedis toRedis(PairingState src, @Context Long ttlSeconds);

    @Mapping(target = "ttlSeconds", ignore = true)
    PairingState toDomain(PairingStateRedis src, @Context Long overrideTtlSeconds);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget PairingStateRedis target,
                     knemognition.heartauth.orchestrator.external.model.FlowStatus status,
                     String reason);

    @AfterMapping
    default void afterToRedis(PairingState src, @MappingTarget PairingStateRedis tgt, @Context Long ttlSeconds) {
        if (ttlSeconds != null) tgt.setTtlSeconds(ttlSeconds);
    }

    @AfterMapping
    default void afterToDomain(PairingStateRedis src, @MappingTarget PairingState tgt, @Context Long overrideTtlSeconds) {
        tgt.setTtlSeconds(overrideTtlSeconds != null ? overrideTtlSeconds : src.getTtlSeconds());
    }
}
