package knemognition.heartauth.orchestrator.modelaction.infrastructure.persistence.store;

import knemognition.heartauth.orchestrator.modelaction.app.mappers.ModelActionMapper;
import knemognition.heartauth.orchestrator.modelaction.app.ports.ModelActionStore;
import knemognition.heartauth.orchestrator.modelaction.domain.ModelAction;
import knemognition.heartauth.orchestrator.modelaction.infrastructure.persistence.entity.ModelActionEntity;
import knemognition.heartauth.orchestrator.modelaction.infrastructure.persistence.repository.ModelActionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ModelActionStoreImpl implements ModelActionStore {

    private final ModelActionRepository repository;
    private final ModelActionMapper entityMapper;

    @Override
    public void storeModelAction(UUID correlationId, String json) {
        ModelActionEntity entity = ModelActionEntity.builder()
                .correlationId(correlationId)
                .payload(json)
                .build();
        repository.save(entity);
    }

    @Override
    public List<ModelAction> getModelActions() {
        return repository.findAll()
                .stream()
                .map(entityMapper::toDomain)
                .toList();
    }
}
