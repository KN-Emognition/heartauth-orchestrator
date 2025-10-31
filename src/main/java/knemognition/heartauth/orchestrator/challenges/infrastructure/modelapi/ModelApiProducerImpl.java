package knemognition.heartauth.orchestrator.challenges.infrastructure.modelapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.challenges.app.ports.ModelApiProducer;
import knemognition.heartauth.orchestrator.external.gateways.kafka.modelapi.mapper.ModelApiKafkaMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgPayload;
import knemognition.heartauth.orchestrator.shared.config.kafka.KafkaMessageProducer;
import knemognition.heartauth.orchestrator.shared.config.kafka.ModelApiTopicsProperties;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelApiProducerImpl implements ModelApiProducer {

    private final KafkaMessageProducer producer;
    private final ObjectMapper objectMapper;
    private final ModelApiKafkaMapper mapper;
    private final ModelApiTopicsProperties properties;

    @Override
    public void predict(UUID id, EcgPayload payload) {
        try {
            PredictRequestDto dto = mapper.toPredictRequestDto(payload);
            String message = objectMapper.writeValueAsString(dto);

            String challengeUniqueId = id.toString();
            String correlationId = MDC.get(HeaderNames.MDC_CORRELATION_ID);
            Headers headers = new RecordHeaders()
                    .add(HeaderNames.HEADER_CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8))
                    .add(HeaderNames.HEADER_MODEL_API_UNIQUE_ID, challengeUniqueId.getBytes(StandardCharsets.UTF_8));

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(properties.getRequest(), null, null, correlationId, message, headers);

            producer.send(record);

            log.info("Queued prediction request | topic={} ",
                    properties.getRequest());

        } catch (Exception e) {
            log.error("Failed to build Kafka message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to prepare prediction request for Kafka", e);
        }
    }
}
