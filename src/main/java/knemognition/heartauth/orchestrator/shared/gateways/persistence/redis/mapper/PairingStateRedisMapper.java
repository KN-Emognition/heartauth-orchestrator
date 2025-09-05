package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.mapstruct.*;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PairingStateRedisMapper {


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "exp", ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    PairingStateRedis baseFromCreate(CreatePairing src, UUID id);

    default PairingStateRedis fromCreate(CreatePairing src, UUID id, long ttlSeconds) {
        var now = Instant.now().getEpochSecond();
        var ent = baseFromCreate(src, id);
        ent.setCreatedAt(now);
        ent.setStatus(FlowStatus.CREATED);
        ent.setTtlSeconds(ttlSeconds);
        ent.setExp(now + ttlSeconds);
        return ent;
    }

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "exp", source = "exp"),
            @Mapping(target = "ttl", source = "ttlSeconds")
    })
    CreatedFlowResult toCreatedResult(PairingStateRedis ent);


    PairingState toDomain(PairingStateRedis src);

    FlowStatusDescription toStatus(PairingStateRedis src);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget PairingStateRedis target,
                     knemognition.heartauth.orchestrator.internal.model.FlowStatus status,
                     String reason);



    @Mapping(target = "attestationType",     ignore = true)
    @Mapping(target = "attestationVerdict",  ignore = true)
    @Mapping(target = "attestationPayloadJson", ignore = true)
    void applyEnrichment(@MappingTarget PairingStateRedis target, EnrichDeviceData src);
}
