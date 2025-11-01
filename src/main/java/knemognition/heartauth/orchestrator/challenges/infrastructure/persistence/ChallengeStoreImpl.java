package knemognition.heartauth.orchestrator.challenges.infrastructure.persistence;

import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
import knemognition.heartauth.orchestrator.challenges.api.NoChallengeException;
import knemognition.heartauth.orchestrator.challenges.app.ports.ChallengeStore;
import knemognition.heartauth.orchestrator.challenges.domain.*;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.entity.ChallengeStateRedis;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.mappers.ChallengeStoreMapper;
import knemognition.heartauth.orchestrator.challenges.infrastructure.persistence.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class ChallengeStoreImpl implements ChallengeStore {

    private final ChallengeStateRepository challengeStateRepository;
    private final ChallengeStoreMapper challengeStoreMapper;
    private static final EnumSet<FlowStatus> TERMINAL_STATUSES = EnumSet.of(
            FlowStatus.APPROVED, FlowStatus.DENIED, FlowStatus.EXPIRED
    );

    @Override
    public Optional<ChallengeState> getFlow(UUID id) {
        return challengeStateRepository.findById(id)
                .map(ent -> {
                    long now = Instant.now()
                            .getEpochSecond();
                    if (ent.getExp() != null && ent.getExp() <= now) return null;
                    return challengeStoreMapper.toDomain(ent);
                });
    }

    @Override
    public CreatedChallengeResult createChallenge(CreateChallenge state) {
        boolean recreated = denyPreviousActiveChallenge(state.getTenantId(), state.getUserId());

        var ent = challengeStoreMapper.fromCreate(state);
        ent.setReason(recreated
                ? FlowStatusReason.FLOW_RECREATED
                : FlowStatusReason.FLOW_CREATED);

        challengeStateRepository.save(ent);
        return challengeStoreMapper.toCreatedResult(ent);
    }

    @Override
    public ChallengeState getChallengeStateByCorrelationId(UUID correlationId) {
        return challengeStateRepository
                .findFirstByModelApiTryIdOrderByCreatedAtDesc(correlationId)
                .map(challengeStoreMapper::toDomain)
                .orElseThrow(() -> new NoChallengeException("challenge_not_found"));
    }

    @Override
    public boolean setStatus(StatusChange statusChange) {
        return challengeStateRepository.findById(statusChange.getId())
                .map(ent -> {
                    challengeStoreMapper.applyStatus(ent, statusChange.getStatus(), statusChange.getReason());
                    challengeStateRepository.save(ent);
                    return true;
                })
                .orElse(false);
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
