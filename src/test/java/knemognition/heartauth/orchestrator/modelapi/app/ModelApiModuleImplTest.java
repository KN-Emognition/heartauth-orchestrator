package knemognition.heartauth.orchestrator.modelapi.app;

import knemognition.heartauth.orchestrator.modelapi.app.handlers.GetCombinedModelApiHandler;
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
class ModelApiModuleImplTest {

    @Mock
    private GetCombinedModelApiHandler handler;

    @InjectMocks
    private ModelApiModuleImpl module;

    @Test
    void shouldDelegateCombinedModelApiRequest() {
        Map<String, Object> expected = Map.of("foo", "bar");
        when(handler.handle()).thenReturn(expected);

        Map<String, Object> result = module.getCombinedModelApi();

        assertThat(result).isEqualTo(expected);
        verify(handler).handle();
    }
}
