package knemognition.heartauth.orchestrator.external.gateways.persistence.mapper;

import knemognition.heartauth.orchestrator.external.app.domain.EnrichDeviceData;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EnrichDeviceMapper {

    @Mapping(target = "attestationType", ignore = true)
    @Mapping(target = "attestationVerdict", ignore = true)
    @Mapping(target = "attestationPayloadJson", ignore = true)
    void applyEnrichment(@MappingTarget PairingStateRedis target, EnrichDeviceData src);
}

