package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalChallengeStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.InternalChallengeStoreMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.constants.FlowStatusReason;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InternalChallengeStoreImpl implements InternalChallengeStore {

    private static final EnumSet<FlowStatus> TERMINAL_STATUSES = EnumSet.of(
            FlowStatus.APPROVED, FlowStatus.DENIED, FlowStatus.EXPIRED
    );

    private final InternalChallengeStoreMapper createChallengeStoreMapper;
    private final ChallengeStateRepository challengeStateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreatedFlowResult createChallenge(CreateChallenge state) {
        boolean recreated = denyPreviousActiveChallenge(state.getTenantId(), state.getUserId());

        var ent = createChallengeStoreMapper.fromCreate(state);
        ent.setReason(recreated
                ? FlowStatusReason.FLOW_RECREATED
                : FlowStatusReason.FLOW_CREATED);

        challengeStateRepository.save(ent);
        return createChallengeStoreMapper.toCreatedResult(ent);
    }

    private boolean denyPreviousActiveChallenge(UUID tenantId, UUID userId) {
        List<ChallengeStateRedis> actives = challengeStateRepository
                .findAllByTenantIdAndUserIdOrderByCreatedAtDesc(tenantId, userId)
                .stream()
                .filter(this::isActive)
                .toList();

        for (ChallengeStateRedis existing : actives) {
            existing.setStatus(FlowStatus.DENIED);
            existing.setReason(FlowStatusReason.FLOW_DENIED_BY_RECREATING_FLOW);
            challengeStateRepository.save(existing);
        }

        return !actives.isEmpty();
    }

    private boolean isActive(ChallengeStateRedis state) {
        FlowStatus status = state.getStatus();
        if (status == null || TERMINAL_STATUSES.contains(status)) {
            return false;
        }
        Long exp = state.getExp();
        return exp == null || exp > Instant.now()
                .getEpochSecond();
    }
}
