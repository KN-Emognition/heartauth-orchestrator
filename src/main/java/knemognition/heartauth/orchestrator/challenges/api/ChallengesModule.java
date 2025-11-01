package knemognition.heartauth.orchestrator.challenges.api;

import org.springframework.modulith.NamedInterface;

@NamedInterface
public interface ChallengesModule {
    ChallengeStatusRead getStatus(GetChallengeStatusCmd cmd);

    CreatedChallengeRead create(CreateChallengeCmd cmd);

    boolean complete(CompleteChallengeWithPredictionPayloadCmd cmd);

    void complete(CompleteChallengeWithPredictionResultCmd cmd);
}
