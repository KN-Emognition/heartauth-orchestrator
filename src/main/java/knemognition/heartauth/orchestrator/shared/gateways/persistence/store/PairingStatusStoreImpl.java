package knemognition.heartauth.orchestrator.shared.gateways.persistence.store;

import knemognition.heartauth.orchestrator.shared.app.domain.*;
import knemognition.heartauth.orchestrator.shared.app.ports.out.StatusStore;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.PairingStatusStoreMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PairingStatusStoreImpl implements StatusStore<PairingState> {


    private final PairingStateRepository pairingStateRepository;
    private final PairingStatusStoreMapper pairingStatusStoreMapper;


    @Override
    public boolean setStatus(StatusChange statusChange) {
        return pairingStateRepository.findById(statusChange.getId())
                .map(ent -> {
                    pairingStatusStoreMapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    pairingStateRepository.save(ent);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<FlowStatusDescription> getStatus(UUID id) {
        return pairingStateRepository.findById(id).map(ent -> {
            long now = Instant.now().getEpochSecond();
            if (ent.getExp() != null && ent.getExp() <= now) return null;
            return pairingStatusStoreMapper.toStatus(ent);
        });
    }
}
