package knemognition.heartauth.orchestrator.internal.app.mapper;


import knemognition.heartauth.orchestrator.internal.model.ChallengeStatus;
import org.mapstruct.Mapper;
import knemognition.heartauth.orchestrator.shared.domain.ChallengeState;
import knemognition.heartauth.orchestrator.internal.model.ChallengeStatusResponse;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChallengeStatusMapper {

    @Mapping(target = "state",  source = "state")
    @Mapping(target = "reason", source = "reason")
    ChallengeStatusResponse toResponse(ChallengeState src);

    default ChallengeStatusResponse notFound() {
        var r = new ChallengeStatusResponse();
        r.setState(ChallengeStatus.NOT_FOUND);
        return r;
    }
}
