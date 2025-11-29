//package knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.mappers;
//
//import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
//import knemognition.heartauth.orchestrator.pairings.domain.CreatePairing;
//import knemognition.heartauth.orchestrator.pairings.infrastructure.persistence.entity.PairingStateRedis;
//import knemognition.heartauth.orchestrator.refdatacollect.domain.CreateRefDataCollectProcess;
//import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity.RefDataCollectProcessEntity;
//import knemognition.heartauth.orchestrator.refdatacollect.infrastructure.persistence.entity.RefDataCollectNotificationRedis;
//import org.mapstruct.*;
//
//import java.time.Instant;
//
//@Mapper(componentModel = "spring")
//public interface RefDataCollectStoreMapper {
//
//    @Mapping(target = "id", source = "jti")
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    RefDataCollectProcessEntity toEntity(CreateRefDataCollectProcess createRefDataCollectProcess);
//
//
//    @BeanMapping(
//            qualifiedByName = "createPairing",
//            unmappedTargetPolicy = ReportingPolicy.IGNORE
//    )
//    @Mapping(target = "id", source = "jti")
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "exp", ignore = true)
//    @Mapping(target = "ttlSeconds", ignore = true)
//    RefDataCollectNotificationRedis fromCreate(CreatePairing src);
//
//    @AfterMapping
//    @Named("createPairing")
//    default void fillRest(@MappingTarget PairingStateRedis ent,
//                          CreatePairing src
//    ) {
//        long now = Instant.now()
//                .getEpochSecond();
//        long ttl = src.getTtlSeconds();
//        ent.setCreatedAt(now);
//        ent.setStatus(FlowStatus.CREATED);
//        ent.setTtlSeconds(ttl);
//        ent.setExp(now + ttl);
//    }
//}
