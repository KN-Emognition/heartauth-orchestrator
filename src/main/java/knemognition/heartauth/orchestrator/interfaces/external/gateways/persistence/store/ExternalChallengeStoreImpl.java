package knemognition.heartauth.orchestrator.interfaces.external.gateways.persistence.store;

import knemognition.heartauth.orchestrator.interfaces.external.app.ports.out.ExternalChallengeStore;
import knemognition.heartauth.orchestrator.interfaces.external.gateways.persistence.mapper.ExternalChallengeStoreMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.StatusChange;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.redis.repository.ChallengeStateRepository;
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
