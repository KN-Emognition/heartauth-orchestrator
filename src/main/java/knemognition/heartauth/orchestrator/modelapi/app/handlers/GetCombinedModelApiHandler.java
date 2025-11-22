package knemognition.heartauth.orchestrator.modelapi.app.handlers;


import knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.CombinedStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class GetCombinedModelApiHandler {
    private final CombinedStore combinedStore;

    public Map<String, Object> handle() {
        log.info("Handling GetCombinedModelApi request");
        // Implement the logic to get the combined model API
        return combinedStore.getStoreAsMap();
    }
}
