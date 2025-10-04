package knemognition.heartauth.orchestrator.external.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExternalPairingStoreMapper {


    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void applyEnrichment(@MappingTarget PairingStateRedis target, EnrichDeviceData src);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    void applyStatus(@MappingTarget PairingStateRedis target,
                     FlowStatus status,
                     String reason);
}

