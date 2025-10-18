package knemognition.heartauth.orchestrator.external.gateways.kafka.modelapi.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiKafka;
import knemognition.heartauth.orchestrator.external.config.kafka.KafkaMessageProducer;
import knemognition.heartauth.orchestrator.external.config.kafka.ModelApiProducerProperties;
import knemognition.heartauth.orchestrator.external.gateways.kafka.modelapi.mapper.ModelApiKafkaMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgPrediction;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(ModelApiProducerProperties.class)
public class ModelApiProducerImpl implements ModelApiKafka {

    private final KafkaMessageProducer producer;
    private final ObjectMapper objectMapper;
    private final ModelApiKafkaMapper mapper;
    private final ModelApiProducerProperties properties;

    @Override
    public void predict(EcgPrediction prediction) {
        try {
            PredictRequestDto dto = mapper.toPredictRequestDto(prediction);
            String payload = objectMapper.writeValueAsString(dto);

            String correlationId = MDC.get(HeaderNames.MDC_CORRELATION_ID);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            Headers headers = new RecordHeaders()
                    .add(HeaderNames.HEADER_CORRELATION_ID, correlationId.getBytes(StandardCharsets.UTF_8));

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(properties.getRequest(), null, null, null, payload, headers);

            producer.send(record);

            log.info("Queued prediction request | topic={} | correlationId={}",
                    properties.getRequest(), correlationId);

        } catch (Exception e) {
            log.error("Failed to build Kafka message: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to prepare prediction request for Kafka", e);
        }
    }
}
