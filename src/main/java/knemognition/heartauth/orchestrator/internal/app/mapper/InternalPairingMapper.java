package knemognition.heartauth.orchestrator.internal.app.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.CreatePairingRequestDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.FlowStatusDto;
import knemognition.heartauth.orchestrator.internal.interfaces.rest.v1.model.StatusResponseDto;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InternalPairingMapper {

    CreatePairing toCreatePairing(UUID tenantId, CreatePairingRequestDto req, UUID jti, Integer ttlSeconds);


    default StatusResponseDto notFoundStatus() {
        var r = new StatusResponseDto();
        r.setStatus(FlowStatusDto.NOT_FOUND);
        return r;
    }

    StatusResponseDto toStatusResponseDto(PairingState state);
}

