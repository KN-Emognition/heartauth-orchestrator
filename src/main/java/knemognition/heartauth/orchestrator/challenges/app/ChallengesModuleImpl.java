package knemognition.heartauth.orchestrator.challenges.app;

import knemognition.heartauth.orchestrator.challenges.api.*;
import knemognition.heartauth.orchestrator.challenges.app.handlers.CompleteChallengeHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.CreateChallengeHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.GetChallengeStatusHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengesModuleImpl implements ChallengesModule {

    private final GetChallengeStatusHandler getChallengeStatusHandler;
    private final CreateChallengeHandler createChallengeHandler;
    private final CompleteChallengeHandler completeChallengeHandler;

    @Override
    public ChallengeStatusRead getStatus(GetChallengeStatusCmd cmd) {
        return getChallengeStatusHandler.handle(cmd);
    }

    @Override
    public CreatedChallengeRead create(CreateChallengeCmd cmd) {
        return createChallengeHandler.handle(cmd);
    }

    @Override
    public void complete(CompleteChallengeWithPredictionResultCmd cmd) {
        completeChallengeHandler.handle(cmd);
    }

    @Override
    public boolean complete(CompleteChallengeWithPredictionPayloadCmd cmd) {
        return completeChallengeHandler.handle(cmd);
    }
}
