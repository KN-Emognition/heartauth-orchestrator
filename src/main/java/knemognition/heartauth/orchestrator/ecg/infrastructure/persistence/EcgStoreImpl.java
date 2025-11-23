package knemognition.heartauth.orchestrator.ecg.infrastructure.persistence;

import knemognition.heartauth.orchestrator.ecg.app.ports.EcgStore;
import knemognition.heartauth.orchestrator.ecg.domain.RefEcg;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.entity.EcgRefDataEntity;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.mappers.EcgRefMapper;
import knemognition.heartauth.orchestrator.ecg.infrastructure.persistence.repository.EcgRefDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EcgStoreImpl implements EcgStore {
    private final EcgRefDataRepository ecgRefDataRepository;
    private final EcgRefMapper ecgRefMapper;

    @Override
    public Optional<RefEcg> getReferenceEcg(UUID userId) {
        return ecgRefDataRepository.findByUserId(userId)
                .map(ecgRefMapper::toDomain);
    }

    @Override
    public void saveReferenceEcg(UUID userId, RefEcg refEcg) {
        var ety = new EcgRefDataEntity();
        ety.setUserId(userId);
        ety.setRefEcg(refEcg.getRefEcg());
        ecgRefDataRepository.save(ety);
    }

    @Override
    public void updateReferenceEcg(UUID userId, RefEcg refEcg) {
        ecgRefDataRepository.findByUserId(userId)
                .ifPresent(ecgRefDataEntity -> {
                    ecgRefDataEntity.setRefEcg(refEcg.getRefEcg());
                    ecgRefDataRepository.save(ecgRefDataEntity);

                });
    }
}
