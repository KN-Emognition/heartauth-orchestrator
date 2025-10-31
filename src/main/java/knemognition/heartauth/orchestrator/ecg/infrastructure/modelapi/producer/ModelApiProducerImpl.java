package knemognition.heartauth.orchestrator.ecg.infrastructure.modelapi.producer;

import knemognition.heartauth.orchestrator.ecg.app.ports.AsyncModelApi;
import knemognition.heartauth.orchestrator.ecg.domain.EcgPayload;
import knemognition.heartauth.orchestrator.ecg.infrastructure.modelapi.mapper.ModelApiKafkaMapper;
import knemognition.heartauth.orchestrator.shared.config.kafka.ModelApiTopicsProperties;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelApiProducerImpl implements AsyncModelApi {

    private final KafkaTemplate<String, PredictRequestDto> template;
    private final ModelApiKafkaMapper mapper;
    private final ModelApiTopicsProperties properties;

    @Override
    public void predict(UUID id, EcgPayload payload) {
        PredictRequestDto dto = mapper.toPredictRequestDto(payload);

        String correlationId = Optional.ofNullable(
                        MDC.get(HeaderNames.MDC_CORRELATION_ID)
                )
                .orElse(id.toString());

        Message<PredictRequestDto> msg = MessageBuilder
                .withPayload(dto)
                .setHeader(KafkaHeaders.TOPIC, properties.getRequest())
                .setHeader(KafkaHeaders.KEY, correlationId)
                .setHeader(HeaderNames.HEADER_CORRELATION_ID, correlationId)
                .setHeader(HeaderNames.HEADER_MODEL_API_UNIQUE_ID, id.toString())
                .build();

        template.send(msg);
        log.info("Queued prediction request | topic={}", properties.getRequest());
    }
}
