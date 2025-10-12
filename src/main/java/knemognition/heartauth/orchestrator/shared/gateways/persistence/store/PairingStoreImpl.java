package knemognition.heartauth.orchestrator.shared.gateways.persistence.store;

import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.RedisEntityMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class PairingStoreImpl implements GetFlowStore<PairingState> {

    private final PairingStateRepository pairingStateRepository;
    private final RedisEntityMapper redisEntityMapper;

    @Override
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
