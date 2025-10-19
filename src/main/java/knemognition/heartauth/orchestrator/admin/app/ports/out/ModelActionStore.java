package knemognition.heartauth.orchestrator.admin.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.ModelAction;

import java.util.List;
import java.util.UUID;

public interface ModelActionStore {
    void storeModelAction(UUID correlationId, String action);

    List<ModelAction> getModelActions();
}
