package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import knemognition.heartauth.orchestrator.external.app.ports.out.GetFlowStore;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.GetPairingMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.PairingState;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class GetPairingStoreImpl implements GetFlowStore<PairingState> {


    private final PairingStateRepository pairingStateRepository;
    private final GetPairingMapper getPairingMapper;


    @Override
    public Optional<PairingState> getFlow(UUID id) {
        return pairingStateRepository.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;
            return getPairingMapper.toDomain(ent);
        });
    }

}
