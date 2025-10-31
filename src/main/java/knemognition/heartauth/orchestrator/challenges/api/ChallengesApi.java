package knemognition.heartauth.orchestrator.challenges.api;

import knemognition.heartauth.orchestrator.shared.FlowStatus;

import java.util.Optional;

public interface ChallengesApi {
    Optional<FlowStatus> getStatus(GetChallengeStatusQuery q);

    CreatedChallenge create(CreateChallengeCmd cmd);

    boolean setStatus(SetStatusCmd cmd);

    void complete(EcgChallengeSendCmd cmd);
}
