package knemognition.heartauth.orchestrator.shared.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;

import java.util.Optional;
import java.util.UUID;

public interface ChallengeStore {
    Optional<ChallengeState> getChallengeState(UUID challengeId);
}
