package knemognition.heartauth.orchestrator.internal.app.service;


import knemognition.heartauth.orchestrator.internal.app.mapper.ResponseStatusMapper;
import knemognition.heartauth.orchestrator.internal.app.service.status.GenericStatusService;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import org.springframework.stereotype.Service;

@Service
public class ChallengeStatusServiceImpl extends GenericStatusService<ChallengeState> {
    public ChallengeStatusServiceImpl(
            StatusStore<ChallengeState> challengeStateStatusStore,
            ResponseStatusMapper mapper
    ) {
        super(challengeStateStatusStore, mapper);
    }
}
