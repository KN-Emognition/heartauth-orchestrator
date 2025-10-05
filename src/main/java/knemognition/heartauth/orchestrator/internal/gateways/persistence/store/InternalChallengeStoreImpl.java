package knemognition.heartauth.orchestrator.internal.gateways.persistence.store;

import knemognition.heartauth.orchestrator.internal.app.domain.CreateChallenge;
import knemognition.heartauth.orchestrator.internal.app.domain.CreatedFlowResult;
import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalChallengeStore;
import knemognition.heartauth.orchestrator.internal.gateways.persistence.mapper.InternalChallengeStoreMapper;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class InternalChallengeStoreImpl implements InternalChallengeStore {

    private final InternalChallengeStoreMapper createChallengeStoreMapper;
    private final ChallengeStateRepository challengeStateRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreatedFlowResult createChallenge(CreateChallenge state) {
        var ent = createChallengeStoreMapper.fromCreate(state);
        challengeStateRepository.save(ent);
        return createChallengeStoreMapper.toCreatedResult(ent);
    }

}
