package knemognition.heartauth.orchestrator.shared.app.mapper;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatusDescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseStatusMapper {
    StatusResponse map(FlowStatusDescription description);

    default StatusResponse notFound() {
        var r = new StatusResponse();
        r.setStatus(FlowStatus.NOT_FOUND);
        return r;
    }
}
