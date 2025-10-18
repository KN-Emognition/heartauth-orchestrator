package knemognition.heartauth.orchestrator.internal.interfaces.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalChallengeService;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import knemognition.heartauth.orchestrator.shared.utils.CorrelationIdResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalModelApiListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InternalChallengeService internalChallengeService;

    @KafkaListener(topics = "${model.api.kafka.topics.reply}", groupId = "internal-orchestrator-group")
    public void handlePredict(ConsumerRecord<String, String> rec) throws JsonProcessingException {
        log.info("Received model API prediction response");
        UUID id = CorrelationIdResolver.resolveOrThrow(rec);
        PredictResponseDto to = objectMapper.readValue(rec.value(), PredictResponseDto.class);
        internalChallengeService.completeChallengeWithPrediction(id, to);
    }
}
