package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreatePairing;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalPairingStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.InternalPairingStoreMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.PairingStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.PairingStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class InternalPairingStoreImpl implements InternalPairingStore {

    private static final EnumSet<FlowStatus> TERMINAL_STATUSES = EnumSet.of(FlowStatus.APPROVED, FlowStatus.DENIED,
            FlowStatus.EXPIRED);

    private final InternalPairingStoreMapper createPairingStoreMapper;
    private final PairingStateRepository pairingStateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreatedFlowResult createPairing(CreatePairing state) {
        boolean recreated = denyPreviousActivePairing(state.getTenantId(), state.getUserId());

        PairingStateRedis ent = createPairingStoreMapper.fromCreate(state);
        ent.setReason(recreated ? FlowStatusReason.FLOW_RECREATED : FlowStatusReason.FLOW_CREATED);

        pairingStateRepository.save(ent);
        return createPairingStoreMapper.toCreatedResult(ent);
    }


    private boolean denyPreviousActivePairing(UUID tenantId, UUID userId) {
        List<PairingStateRedis> actives = pairingStateRepository.findAllByTenantIdAndUserIdOrderByCreatedAtDesc(
                        tenantId, userId)
                .stream()
                .filter(this::isActive)
                .toList();

        for (PairingStateRedis existing : actives) {
            existing.setStatus(FlowStatus.DENIED);
            existing.setReason(FlowStatusReason.FLOW_DENIED_BY_RECREATING_FLOW);
            pairingStateRepository.save(existing);
        }

        return !actives.isEmpty();
    }

    private boolean isActive(PairingStateRedis state) {
        FlowStatus status = state.getStatus();
        if (status == null || TERMINAL_STATUSES.contains(status)) {
            return false;
        }
        Long exp = state.getExp();
        return exp == null || exp > Instant.now()
                .getEpochSecond();
    }
}
