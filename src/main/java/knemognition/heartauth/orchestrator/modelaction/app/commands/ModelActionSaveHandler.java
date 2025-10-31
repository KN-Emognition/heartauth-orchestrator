package knemognition.heartauth.orchestrator.modelaction.app.commands;

import knemognition.heartauth.orchestrator.modelaction.api.SaveModelActionCmd;
import knemognition.heartauth.orchestrator.modelaction.app.ports.ModelActionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelActionSaveHandler {
    private final ModelActionStore store;

    public void save(SaveModelActionCmd cmd) {
        store.storeModelAction(cmd.getCorrelationId(), cmd.getPayload());
    }
}
