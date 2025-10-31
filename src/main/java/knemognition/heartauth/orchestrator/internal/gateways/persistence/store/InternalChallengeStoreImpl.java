package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.challenges.app.exceptions.NoChallengeException;
import knemognition.heartauth.orchestrator.challenges.api.CreateChallengeCmd;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalChallengeStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.InternalChallengeStoreMapper;
import knemognition.heartauth.orchestrator.challenges.domain.ChallengeState;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import knemognition.heartauth.orchestrator.user.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.FlowStatusReason;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRepository;
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

    private final InternalChallengeStoreMapper internalChallengeStoreMapper;
    private final ChallengeStateRepository challengeStateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreatedFlowResult createChallenge(CreateChallengeCmd state) {
        boolean recreated = denyPreviousActiveChallenge(state.getTenantId(), state.getUserId());

        var ent = internalChallengeStoreMapper.fromCreate(state);
        ent.setReason(recreated
                ? FlowStatusReason.FLOW_RECREATED
                : FlowStatusReason.FLOW_CREATED);

        challengeStateRepository.save(ent);
        return internalChallengeStoreMapper.toCreatedResult(ent);
    }

    @Override
    public ChallengeState getChallengeStateByCorrelationId(UUID correlationId) {
        return challengeStateRepository
                .findFirstByModelApiTryIdOrderByCreatedAtDesc(correlationId)
                .map(internalChallengeStoreMapper::toDomain)
                .orElseThrow(() -> new NoChallengeException("challenge_not_found"));
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

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return challengeStateRepository.findById(statusChange.getId())
                .map(ent -> {
                    internalChallengeStoreMapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    challengeStateRepository.save(ent);
                    return true;
                })
                .orElse(false);
    }
}
