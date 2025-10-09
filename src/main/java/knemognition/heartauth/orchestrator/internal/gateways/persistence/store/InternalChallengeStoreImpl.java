package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalChallengeStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.InternalChallengeStoreMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.model.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InternalChallengeStoreImpl implements InternalChallengeStore {

    static final String PREVIOUS_CHALLENGE_SUPERSEDED_REASON = "Superseded by new challenge request";
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
        denyPreviousActiveChallenge(state.getTenantId(), state.getUserId());
        var ent = createChallengeStoreMapper.fromCreate(state);
        challengeStateRepository.save(ent);
        return createChallengeStoreMapper.toCreatedResult(ent);
    }

    private void denyPreviousActiveChallenge(UUID tenantId, UUID userId) {
        challengeStateRepository.findTopByTenantIdAndUserIdOrderByCreatedAtDesc(tenantId, userId)
                .filter(this::isActive)
                .ifPresent(existing -> {
                    existing.setStatus(FlowStatus.DENIED);
                    existing.setReason(PREVIOUS_CHALLENGE_SUPERSEDED_REASON);
                    challengeStateRepository.save(existing);
                });
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
