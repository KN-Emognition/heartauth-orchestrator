package knemognition.heartauth.orchestrator.shared.gateways.persistence.store;

import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.ChallengeStatusMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChallengeStatusStoreImpl implements StatusStore<ChallengeState> {

    private final ChallengeStateRepository challengeStateRepository;
    private final ChallengeStatusMapper challengeStatusMapper;

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return challengeStateRepository.findById(statusChange.getId())
                .map(ent -> {
                    challengeStatusMapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    // No manual TTL juggling; Redis preserves remaining TTL on update.
                    challengeStateRepository.save(ent);
                    return true;
                })
                .orElse(false);
    }


    @Override
    public Optional<FlowStatusDescription> getStatus(UUID id) {
        return challengeStateRepository.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;
            return challengeStatusMapper.toStatus(ent);
        });
    }
}
