package knemognition.heartauth.orchestrator.shared.app.redis;


import knemognition.heartauth.orchestrator.shared.app.api.ChallengeStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import knemognition.heartauth.orchestrator.shared.app.domain.ChallengeState;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChallengeStoreImpl implements ChallengeStore {
    private final RedisTemplate<UUID, ChallengeState> challengeStateRedisTemplate;

    public ChallengeStoreImpl(
            @Qualifier("challengeStateRedisTemplate")
            RedisTemplate<UUID, ChallengeState> challengeStateRedisTemplate
    ) {
        this.challengeStateRedisTemplate = challengeStateRedisTemplate;
    }
    @Override
    public void create(ChallengeState st, Duration ttl) {
        UUID key = st.getChallengeId();

        Boolean ok = challengeStateRedisTemplate.opsForValue().setIfAbsent(key, st, ttl);
        if (ok == null || !ok) {
            throw new IllegalStateException("conflict");
        }
    }

    @Override
    public Optional<ChallengeState> get(UUID challengeId) {
        ChallengeState st = challengeStateRedisTemplate.opsForValue().get(challengeId);
        if (st == null) return Optional.empty();

        long now = Instant.now().getEpochSecond();
        if (st.getExp() != null && st.getExp() <= now) {
            return Optional.empty();
        }
        return Optional.of(st);
    }

    @Override
    public boolean changeState(UUID challengeId, String newState, String reason) {
        ChallengeState current = challengeStateRedisTemplate.opsForValue().get(challengeId);
        if (current == null) return false;

        current.setState(newState);
        current.setReason(reason);

        Long sec = challengeStateRedisTemplate.getExpire(challengeId, java.util.concurrent.TimeUnit.SECONDS);
        if (sec != null && sec > 0) {
            return Boolean.TRUE.equals(
                    challengeStateRedisTemplate.opsForValue().setIfPresent(challengeId, current, java.time.Duration.ofSeconds(sec))
            );
        } else {
            return Boolean.TRUE.equals(
                    challengeStateRedisTemplate.opsForValue().setIfPresent(challengeId, current)
            );
        }
    }
}

