package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalPairingStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.InternalPairingStoreMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class InternalPairingStoreImpl implements InternalPairingStore {

    private final InternalPairingStoreMapper createPairingStoreMapper;
    private final PairingStateRepository pairingStateRepository;

    @Override
    public CreatedFlowResult createPairing(CreatePairing state) {
        var ent = createPairingStoreMapper.fromCreate(state);
        pairingStateRepository.save(ent);
        return createPairingStoreMapper.toCreatedResult(ent);
    }
}
