package knemognition.heartauth.orchestrator.internal.app.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = FlowStatus.class, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CreatePairingMapper {

    @Mapping(target = "ttlSeconds", source = "effectiveTtl")
    CreatePairing toCreatePairing(PairingCreateRequest req, UUID jti, Integer effectiveTtl);
}

