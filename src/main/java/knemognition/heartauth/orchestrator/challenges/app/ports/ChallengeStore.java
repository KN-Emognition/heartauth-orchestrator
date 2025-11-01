package knemognition.heartauth.orchestrator.challenges.app.ports;

import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.challenges.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.challenges.domain.CreatedChallengeResult;
import knemognition.heartauth.orchestrator.challenges.domain.StatusChange;

import java.util.Optional;
import java.util.UUID;

public interface ChallengeStore {
    Optional<ChallengeState> getFlow(UUID id);

    CreatedChallengeResult createChallenge(CreateChallenge state);

    ChallengeState getChallengeStateByCorrelationId(UUID correlationId);

    boolean setStatus(StatusChange statusChange);
}
