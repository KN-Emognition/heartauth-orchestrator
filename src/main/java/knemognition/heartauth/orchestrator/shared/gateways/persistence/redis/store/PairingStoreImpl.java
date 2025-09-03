package knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.store;

import knemognition.heartauth.orchestrator.external.model.FlowStatus;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.FlowStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.mapper.PairingStateRedisMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
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
public class PairingStoreImpl implements FlowStore<PairingState> {

    private static final String KEYSPACE = "pairing:";

    private final PairingStateRepository repo;
    private final StringRedisTemplate redis;
    private final PairingStateRedisMapper mapper;

    @Override
    public void create(PairingState st, Duration ttl) {
        if (st.getId() == null) throw new IllegalArgumentException("id (jti) required");
        Long ttlSec = ttl != null ? ttl.getSeconds() : null;
        PairingStateRedis ent = mapper.toRedis(st, ttlSec);
        if (repo.existsById(ent.getId())) throw new IllegalStateException("pairing_conflict");
        repo.save(ent);
    }

    @Override
    public Optional<PairingState> get(UUID id) {
        return repo.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;

            Long remaining = redis.getExpire(KEYSPACE + id, TimeUnit.SECONDS);
            Long effTtl = (remaining != null && remaining > 0) ? remaining : ent.getTtlSeconds();

            return mapper.toDomain(ent, effTtl);
        }).filter(ps -> ps != null);
    }

    @Override
    public boolean setStatus(UUID id, FlowStatus newStatus, String reason) {
        return repo.findById(id).map(ent -> {
            mapper.applyStatus(ent, newStatus, reason);

            Long remaining = redis.getExpire(KEYSPACE + id, TimeUnit.SECONDS);
            if (remaining != null && remaining > 0) ent.setTtlSeconds(remaining);

            repo.save(ent);
            return true;
        }).orElse(false);
    }

    public boolean markLinkedAndShrinkTtl(UUID id, long keepSeconds) {
        return repo.findById(id).map(ent -> {
            mapper.applyStatus(ent, FlowStatus.APPROVED, ent.getReason());
            Long cur = redis.getExpire(KEYSPACE + id, TimeUnit.SECONDS);
            long newTtl = (cur != null && cur > 0) ? Math.min(cur, keepSeconds) : keepSeconds;
            ent.setTtlSeconds(newTtl);
            repo.save(ent);
            return true;
        }).orElse(false);
    }
}
