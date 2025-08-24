package knemognition.heartauth.orchestrator.shared.api;



import knemognition.heartauth.orchestrator.shared.domain.ChallengeState;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeStore {
    void create(ChallengeState state, Duration ttl);
    Optional<ChallengeState> get(UUID challengeId);

    boolean changeState(UUID challengeId, String newState, String reason);
}