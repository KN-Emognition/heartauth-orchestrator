package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.store;

import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper.ChallengeStateRedisMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ChallengeStoreImpl implements StatusStore<ChallengeState>, CreateFlowStore<CreateChallenge> {

    private static final long DEFAULT_TTL_SECONDS = 300L;

    private final ChallengeStateRepository repo;
    private final ChallengeStateRedisMapper mapper;

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return repo.findById(statusChange.getId())
                .map(ent -> {
                    mapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    // No manual TTL juggling; Redis preserves remaining TTL on update.
                    repo.save(ent);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public CreatedFlowResult create(CreateChallenge state) {
        final UUID id = UUID.randomUUID();
        final long ttl = Optional.ofNullable(state.getTtlSeconds()).filter(v -> v > 0).orElse(DEFAULT_TTL_SECONDS);

        var ent = mapper.fromCreate(state, id, ttl); // fills createdAt, exp, ttlSeconds, status
        repo.save(ent);                               // @TimeToLive drives actual Redis expiry

        return mapper.toCreatedResult(ent);
    }

    @Override
    public Optional<FlowStatusDescription> getStatus(UUID id) {
        return repo.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;
            return mapper.toStatus(ent);
        });
    }
}
