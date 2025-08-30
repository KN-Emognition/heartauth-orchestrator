package knemognition.heartauth.orchestrator.internal.app.mapper;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChallengeStatusMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "reason", source = "reason")
    StatusResponse toResponse(ChallengeState src);

    default StatusResponse notFound() {
        var r = new StatusResponse();
        r.setStatus(FlowStatus.NOT_FOUND);
        return r;
    }
}
