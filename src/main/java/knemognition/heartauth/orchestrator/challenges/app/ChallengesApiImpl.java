package knemognition.heartauth.orchestrator.challenges.app;

import knemognition.heartauth.orchestrator.challenges.api.ChallengesApi;
import knemognition.heartauth.orchestrator.challenges.api.CreatedChallenge;
import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.challenges.api.EcgChallengeSendCmd;
import knemognition.heartauth.orchestrator.challenges.api.SetStatusCmd;
import knemognition.heartauth.orchestrator.challenges.api.GetChallengeStatusQuery;
import knemognition.heartauth.orchestrator.challenges.app.handlers.CompleteChallengeHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.CreateChallengeHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.GetChallengeStatusHandler;
import knemognition.heartauth.orchestrator.challenges.app.handlers.SetStatusHandler;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengesApiImpl implements ChallengesApi {

    private final CreateChallengeHandler createHandler;
    private final CompleteChallengeHandler completeHandler;
    private final SetStatusHandler setStatusHandler;
    private final GetChallengeStatusHandler getStatusHandler;

    @Override
    public CreatedChallenge create(CreateChallengeCmd cmd) {
        return createHandler.handle(cmd);
    }

    @Override
    public void complete(EcgChallengeSendCmd cmd) {
        completeHandler.handle(cmd);
    }

    @Override
    public boolean setStatus(SetStatusCmd cmd) {
        return setStatusHandler.handle(cmd);
    }

    @Override
    public Optional<FlowStatus> getStatus(GetChallengeStatusQuery q) {
        return getStatusHandler.handle(q);
    }
}
