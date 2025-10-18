package knemognition.heartauth.orchestrator.admin.gateways.persistence.store;

import knemognition.heartauth.orchestrator.admin.app.ports.out.ModelActionStore;
import knemognition.heartauth.orchestrator.shared.app.domain.ModelAction;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.entity.ModelActionEntity;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.jpa.repository.ModelActionRepository;
import knemognition.heartauth.orchestrator.shared.gateways.persistence.mapper.MainStoreMapper;
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
    private final MainStoreMapper entityMapper;

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
