package knemognition.heartauth.orchestrator.pairing.redis;

import knemognition.heartauth.orchestrator.pairing.redis.mapper.PairingStoreMapper;
import knemognition.heartauth.orchestrator.pairing.redis.repository.PairingStateRepository;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class PairingStore {

    private final PairingStateRepository pairingStateRepository;
    private final PairingStoreMapper redisEntityMapper;

    public Optional<PairingState> getFlow(UUID id) {
        return pairingStateRepository.findById(id)
                .map(ent -> {
                    long now = Instant.now()
                            .getEpochSecond();
                    if (ent.getExp() != null && ent.getExp() <= now) return null;
                    return redisEntityMapper.toDomain(ent);
                });
    }
}
