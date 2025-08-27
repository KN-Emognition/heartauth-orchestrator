package knemognition.heartauth.orchestrator.shared.app.ports.out;



import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeStore {
    void create(ChallengeState state, Duration ttl);
    Optional<ChallengeState> get(UUID challengeId);

    boolean changeStatus(UUID id, FlowStatus newStatus, String reason);
}