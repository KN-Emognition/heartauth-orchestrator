package knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import knemognition.heartauth.orchestrator.pairing.redis.model.PairingStateRedis;
import org.mapstruct.*;

import java.time.Instant;


@Mapper(componentModel = "spring")
public interface InternalPairingStoreMapper {


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

    CreatedFlowResult toCreatedResult(PairingStateRedis ent);


}
