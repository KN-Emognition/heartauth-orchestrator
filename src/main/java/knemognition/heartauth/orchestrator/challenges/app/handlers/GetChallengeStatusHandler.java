package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.GetChallengeStatusQuery;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRepository;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetChallengeStatusHandler {
    private final ChallengeStateRepository repo;

    public Optional<FlowStatus> handle(GetChallengeStatusQuery q) {
        return repo.findById(q.getId())
                .map(e -> {
                    long now = Instant.now()
                            .getEpochSecond();
                    if (e.getExp() != null && e.getExp() <= now) return FlowStatus.NOT_FOUND;
                    return e.getStatus();
                });
    }
}
