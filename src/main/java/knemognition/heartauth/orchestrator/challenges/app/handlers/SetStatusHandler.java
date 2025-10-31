package knemognition.heartauth.orchestrator.challenges.app.handlers;

import knemognition.heartauth.orchestrator.challenges.api.SetStatusCmd;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetStatusHandler {
    private final ChallengeStateRepository repo;

    public boolean handle(SetStatusCmd cmd) {
        return repo.findById(cmd.getId())
                .map(e -> {
                    e.setStatus(cmd.getStatus());
                    e.setReason(cmd.getReason());
                    repo.save(e);
                    return true;
                })
                .orElse(false);
    }
}