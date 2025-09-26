package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.CreateFlowStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.CreateChallengeStoreMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class CreateChallengeStoreImpl implements CreateFlowStore<CreateChallenge> {

    private static final long DEFAULT_TTL_SECONDS = 300L;

    private final ChallengeStateRepository challengeStateRepository;
    private final CreateChallengeStoreMapper createChallengeStoreMapper;


    @Override
    public CreatedFlowResult create(CreateChallenge state) {
        final UUID id = UUID.randomUUID();
        final long ttl = Optional.ofNullable(state.getTtlSeconds()).filter(v -> v > 0).orElse(DEFAULT_TTL_SECONDS);

        var ent = createChallengeStoreMapper.fromCreate(state, id, ttl);
        challengeStateRepository.save(ent);

        return createChallengeStoreMapper.toCreatedResult(ent);
    }

}
