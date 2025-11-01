package knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.mappers;

import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.pairings.domain.CreatePairing;
import knemognition.heartauth.orchestrator.pairings.domain.CreatedPairingResult;
import knemognition.heartauth.orchestrator.pairings.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.pairings.domain.PairingState;
import knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.entity.PairingStateRedis;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface PairingStoreMapper {
    PairingState toDomain(PairingStateRedis entity);

    @BeanMapping(
            qualifiedByName = "createPairing",
            unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    @Mapping(target = "id", source = "jti")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "exp", ignore = true)
    @Mapping(target = "ttlSeconds", ignore = true)
    PairingStateRedis fromCreate(CreatePairing src);

    @AfterMapping
    @Named("createPairing")
    default void fillRest(@MappingTarget PairingStateRedis ent,
                          CreatePairing src
    ) {
        long now = Instant.now()
                .getEpochSecond();
        long ttl = src.getTtlSeconds();
        ent.setCreatedAt(now);
        ent.setStatus(FlowStatus.CREATED);
        ent.setTtlSeconds(ttl);
        ent.setExp(now + ttl);
    }

    CreatedPairingResult toResult(PairingStateRedis ent);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void applyEnrichment(@MappingTarget PairingStateRedis target, EnrichDeviceData src);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget PairingStateRedis target,
                     FlowStatus status,
                     String reason);
}
