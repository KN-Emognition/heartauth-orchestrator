package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.producer;

import knemognition.heartauth.orchestrator.modelapi.api.EcgSendPredictCmd;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictRequestDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.mapper.ModelApiKafkaMapper;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModelApiProducerImplTest {

    @Mock
    private KafkaTemplate<String, PredictRequestDto> kafkaTemplate;
    @Mock
    private ModelApiKafkaMapper mapper;
    @Mock
    private PredictRequestDto requestDto;

    @AfterEach
    void cleanupMdc() {
        MDC.clear();
    }

    @Test
    void shouldSendPredictRequestUsingMdcCorrelationId() {
        var correlationId = UUID.fromString("f701e3ce-4a82-4ae3-b2dd-0f7ca1373a3d");
        var payload = EcgSendPredictCmd.builder()
                .correlationId(correlationId)
                .testEcg(List.of(0.1f))
                .refEcg(List.of(List.of(0.2f)))
                .build();
        when(mapper.toPredictRequestDto(payload)).thenReturn(requestDto);
        MDC.put(HeaderNames.MDC_CORRELATION_ID, "mdc-corr");
        var producer = new ModelApiProducerImpl(kafkaTemplate, mapper, "model-topic");

        producer.handle(payload);

        ArgumentCaptor<Message<PredictRequestDto>> captor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(captor.capture());
        Message<PredictRequestDto> message = captor.getValue();
        assertThat(message.getHeaders().get(HeaderNames.HEADER_CORRELATION_ID)).isEqualTo("mdc-corr");
        assertThat(message.getHeaders().get(HeaderNames.HEADER_MODEL_API_UNIQUE_ID)).isEqualTo(correlationId.toString());
        assertThat(message.getHeaders().get(org.springframework.kafka.support.KafkaHeaders.TOPIC)).isEqualTo("model-topic");
        assertThat(message.getPayload()).isEqualTo(requestDto);
    }

    @Test
    void shouldFallbackToPayloadCorrelationIdWhenMdcMissing() {
        var correlationId = UUID.fromString("ad63d538-1347-4c66-9df2-7d151d173a90");
        var payload = EcgSendPredictCmd.builder()
                .correlationId(correlationId)
                .testEcg(List.of(0.3f))
                .refEcg(List.of(List.of(0.4f)))
                .build();
        when(mapper.toPredictRequestDto(payload)).thenReturn(requestDto);
        var producer = new ModelApiProducerImpl(kafkaTemplate, mapper, "model-topic");

        producer.handle(payload);

        ArgumentCaptor<Message<PredictRequestDto>> captor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(captor.capture());
        Message<PredictRequestDto> message = captor.getValue();
        assertThat(message.getHeaders().get(HeaderNames.HEADER_CORRELATION_ID)).isEqualTo(correlationId.toString());
    }
}
