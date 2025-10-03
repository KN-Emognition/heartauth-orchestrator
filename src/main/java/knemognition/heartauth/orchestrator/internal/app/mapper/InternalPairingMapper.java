package knemognition.heartauth.orchestrator.internal.app.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.model.CreatePairingRequestDto;
import knemognition.heartauth.orchestrator.internal.model.FlowStatusDto;
import knemognition.heartauth.orchestrator.internal.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InternalPairingMapper {

    CreatePairing toCreatePairing(UUID tenantId, CreatePairingRequestDto req, UUID jti, Integer ttlSeconds);


    StatusResponseDto map(FlowStatusDescription description);

    default StatusResponseDto notFoundStatus() {
        var r = new StatusResponseDto();
        r.setStatus(FlowStatusDto.NOT_FOUND);
        return r;
    }
}

