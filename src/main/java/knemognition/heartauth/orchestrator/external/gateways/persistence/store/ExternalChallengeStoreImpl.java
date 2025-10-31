package knemognition.heartauth.orchestrator.external.gateways.persistence.store;

import knemognition.heartauth.orchestrator.external.app.ports.out.ExternalChallengeStore;
import knemognition.heartauth.orchestrator.external.gateways.persistence.mapper.ExternalChallengeStoreMapper;
import knemognition.heartauth.orchestrator.user.domain.StatusChange;
import knemognition.heartauth.orchestrator.challenges.infrastructure.redis.ChallengeStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalChallengeStoreImpl implements ExternalChallengeStore {

    private final ExternalChallengeStoreMapper internalChallengeStoreMapper;
    private final ChallengeStateRepository challengeStateRepository;

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
