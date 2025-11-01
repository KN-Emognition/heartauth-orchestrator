package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.ChallengeStatusRead;
import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.api.GetChallengeStatusCmd;
import knemognition.heartauth.orchestrator.challenges.app.mappers.ChallengesMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetChallengeStatusHandler {
    private final ChallengeStore challengeStore;
    private final ChallengesMapper challengesMapper;

    public ChallengeStatusRead handle(GetChallengeStatusCmd cmd) {
        Optional<ChallengeState> state = challengeStore.getFlow(cmd.getChallengeId());
        log.info("[CHALLENGE] Queried state for {}", cmd.getChallengeId());

        if (state.isEmpty() || !cmd.getTenantId()
                .equals(state.get()
                        .getTenantId())) {
            log.info("[CHALLENGE] Tenant can't access this state");
            return ChallengeStatusRead.builder()
                    .status(FlowStatus.NOT_FOUND)
                    .build();
        }

        return challengesMapper.toRead(state.get());
    }
}
