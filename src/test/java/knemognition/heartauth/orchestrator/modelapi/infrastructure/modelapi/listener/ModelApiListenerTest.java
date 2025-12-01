package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.listener;

import knemognition.heartauth.orchestrator.modelapi.api.CompleteChallengeWithPredictionResultCmd;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiCallbackApi;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.CombinedModelActionDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictResponseDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.mapper.ModelApiKafkaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModelApiListenerTest {

    @Mock
    private ModelApiKafkaMapper mapper;
    @Mock
    private ModelApiCallbackApi callbackApi;

    @InjectMocks
    private ModelApiListener listener;

    @Test
    void shouldMapAndForwardPredictResponses() {
        PredictResponseDto payload = mock(PredictResponseDto.class);
        UUID uniqueId = UUID.fromString("12aa4f2b-266b-4821-b8f6-4a3b05abf084");
        CompleteChallengeWithPredictionResultCmd cmd = CompleteChallengeWithPredictionResultCmd.builder()
                .correlationId(uniqueId)
                .prediction(true)
                .build();
        when(mapper.toCmd(payload, uniqueId)).thenReturn(cmd);

        listener.handlePredict(payload, uniqueId.toString(), null);

        verify(callbackApi).handle(cmd);
    }
}
