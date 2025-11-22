package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.producer;

import knemognition.heartauth.orchestrator.modelapi.api.EcgSendPredictCmd;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiSendApi;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictRequestDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.mapper.ModelApiKafkaMapper;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ModelApiProducerImpl implements ModelApiSendApi {

    private final KafkaTemplate<String, PredictRequestDto> template;
    private final ModelApiKafkaMapper mapper;
    private final String topic;

    public ModelApiProducerImpl(KafkaTemplate<String, PredictRequestDto> template, ModelApiKafkaMapper mapper, @Value("${model.api.topics.request}") String topic) {
        this.template = template;
        this.mapper = mapper;
        this.topic = topic;
    }

    @Override
    public void handle(EcgSendPredictCmd payload) {
        PredictRequestDto dto = mapper.toPredictRequestDto(payload);

        String correlationId = Optional.ofNullable(MDC.get(HeaderNames.MDC_CORRELATION_ID))
                .orElse(payload.getCorrelationId()
                        .toString());

        Message<PredictRequestDto> msg = MessageBuilder.withPayload(dto)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, correlationId)
                .setHeader(HeaderNames.HEADER_CORRELATION_ID, correlationId)
                .setHeader(HeaderNames.HEADER_MODEL_API_UNIQUE_ID, payload.getCorrelationId()
                        .toString())
                .build();

        template.send(msg);
        log.info("[MODELAPI] Queued prediction request | topic={}", topic);
    }
}
