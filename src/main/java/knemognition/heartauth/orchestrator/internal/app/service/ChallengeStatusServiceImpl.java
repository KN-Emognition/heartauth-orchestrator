package knemognition.heartauth.orchestrator.internal.app.service;

import knemognition.heartauth.orchestrator.internal.app.ports.in.StatusService;
import knemognition.heartauth.orchestrator.internal.model.StatusResponse;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.FlowStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import knemognition.heartauth.orchestrator.internal.app.mapper.ChallengeStatusMapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeStatusServiceImpl implements StatusService {
    private final FlowStore<ChallengeState> store;
    private final ChallengeStatusMapper mapper;

    @Override
    public StatusResponse status(UUID challengeId) {
        StatusResponse response = store.get(challengeId)
                .map(mapper::toResponse)
                .orElseGet(mapper::notFound);

        log.info("Queried status for challenge {}", challengeId);
        return response;
    }
}
