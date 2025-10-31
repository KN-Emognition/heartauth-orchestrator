package knemognition.heartauth.orchestrator.modelaction.app.ports;


import knemognition.heartauth.orchestrator.modelaction.domain.ModelAction;

import java.util.List;
import java.util.UUID;

public interface ModelActionStore {
    void storeModelAction(UUID correlationId, String action);

    List<ModelAction> getModelActions();
}
