package knemognition.heartauth.orchestrator.modelapi.app.handlers;

import knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.CombinedStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCombinedModelApiHandlerTest {

    @Mock
    private CombinedStore combinedStore;

    @InjectMocks
    private GetCombinedModelApiHandler handler;

    @Test
    void shouldReturnCombinedStoreAsMap() {
        Map<String, Object> storeMap = Map.of("combined", 123);
        when(combinedStore.getStoreAsMap()).thenReturn(storeMap);

        Map<String, Object> result = handler.handle();

        assertThat(result).isEqualTo(storeMap);
        verify(combinedStore).getStoreAsMap();
    }
}
