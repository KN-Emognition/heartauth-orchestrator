package knemognition.heartauth.orchestrator.internal.app.mapper;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PairingStatusMapper {

    @Mapping(target = "status", source = "status")
    StatusResponse toResponse(PairingState src);

    default StatusResponse notFound() {
        var r = new StatusResponse();
        r.setStatus(FlowStatus.NOT_FOUND);
        return r;
    }
}
