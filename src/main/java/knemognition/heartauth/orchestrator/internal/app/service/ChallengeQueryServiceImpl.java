package knemognition.heartauth.orchestrator.internal.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import knemognition.heartauth.orchestrator.internal.app.ports.in.ChallengeQueryService;
import knemognition.heartauth.orchestrator.internal.app.mapper.ChallengeStatusMapper;
import knemognition.heartauth.orchestrator.shared.api.ChallengeStore;
import knemognition.heartauth.orchestrator.internal.model.ChallengeStatusResponse;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService {
    private final ChallengeStore store;
    private final ChallengeStatusMapper mapper;

    public ChallengeStatusResponse status(UUID challengeId) {
        return store.get(challengeId)
                .map(mapper::toResponse)
                // key missing (expired/unknown) → treat as expired to match lean spec
                .orElseGet(mapper::notFound);
    }
}