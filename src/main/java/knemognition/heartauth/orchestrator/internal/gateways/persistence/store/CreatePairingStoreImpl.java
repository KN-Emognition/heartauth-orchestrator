package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.CreatePairingStoreMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class CreatePairingStoreImpl implements CreateFlowStore<CreatePairing> {

    private static final long DEFAULT_TTL_SECONDS = 300L;

    private final PairingStateRepository pairingStateRepository;
    private final CreatePairingStoreMapper createPairingStoreMapper;

    @Override
    public CreatedFlowResult create(CreatePairing state) {
        final UUID id = state.getJti();
        final long ttl = Optional.ofNullable(state.getTtlSeconds()).filter(t -> t > 0).orElse(DEFAULT_TTL_SECONDS);
        var ent = createPairingStoreMapper.fromCreate(state, id, ttl);
        pairingStateRepository.save(ent);
        return createPairingStoreMapper.toCreatedResult(ent);
    }
}