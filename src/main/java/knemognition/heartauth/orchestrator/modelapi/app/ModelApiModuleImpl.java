package knemognition.heartauth.orchestrator.modelapi.app;

import knemognition.heartauth.orchestrator.modelapi.api.ModelApiModule;
import knemognition.heartauth.orchestrator.modelapi.app.handlers.GetCombinedModelApiHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ModelApiModuleImpl implements ModelApiModule {

    private final GetCombinedModelApiHandler getCombinedModelApiHandler;

    @Override
    public Map<String, Object> getCombinedModelApi() {
        return getCombinedModelApiHandler.handle();
    }
}
