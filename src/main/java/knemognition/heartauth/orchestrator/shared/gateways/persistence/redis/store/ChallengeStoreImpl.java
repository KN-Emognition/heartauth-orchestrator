package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.store;

import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.ChallengeStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper.ChallengeStateRedisMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ChallengeStoreImpl implements ChallengeStore {

    private static final String KEYSPACE = "challenge:";

    private final ChallengeStateRepository repo;
    private final StringRedisTemplate redis;
    private final ChallengeStateRedisMapper mapper;

    @Override
    public void create(ChallengeState st, Duration ttl) {
        if (st.getId() == null) throw new IllegalArgumentException("id required");
        Long ttlSec = ttl != null ? ttl.getSeconds() : null;
        ChallengeStateRedis ent = mapper.toRedis(st, ttlSec);
        if (repo.existsById(ent.getId())) throw new IllegalStateException("conflict");
        repo.save(ent); // @TimeToLive on entity applies the expiry
    }

    @Override
    public Optional<ChallengeState> get(UUID id) {
        return repo.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;

            Long remaining = redis.getExpire(KEYSPACE + id, TimeUnit.SECONDS);
            Long effTtl = (remaining != null && remaining > 0) ? remaining : ent.getTtlSeconds();

            return mapper.toDomain(ent, effTtl);
        }).filter(cs -> cs != null);
    }

    @Override
    public boolean changeStatus(UUID id, FlowStatus newStatus, String reason) {
        return repo.findById(id).map(ent -> {
            mapper.applyStatus(ent, newStatus, reason);

            Long remaining = redis.getExpire(KEYSPACE + id, TimeUnit.SECONDS);
            if (remaining != null && remaining > 0) ent.setTtlSeconds(remaining);

            repo.save(ent);
            return true;
        }).orElse(false);
    }
}
