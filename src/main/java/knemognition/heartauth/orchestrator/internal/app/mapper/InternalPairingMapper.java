package knemognition.heartauth.orchestrator.internal.app.mapper;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.PairingCreateRequest;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface InternalPairingMapper {

    CreatePairing toCreatePairing(UUID tenantId, PairingCreateRequest req, UUID jti, Integer ttlSeconds);


    StatusResponse map(FlowStatusDescription description);

    default StatusResponse notFoundStatus() {
        var r = new StatusResponse();
        r.setStatus(FlowStatus.NOT_FOUND);
        return r;
    }
}

