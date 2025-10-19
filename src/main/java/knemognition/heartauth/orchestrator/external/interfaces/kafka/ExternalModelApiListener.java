package knemognition.heartauth.orchestrator.external.interfaces.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalModelApiListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${model.api.kafka.topics.reply}", groupId = "external-orchestrator-group")
    public void handlePredict(ConsumerRecord<String, String> rec) throws JsonProcessingException {
        log.info(rec.value());
        PredictResponseDto to = objectMapper.readValue(rec.value(), PredictResponseDto.class);

    }
}
