package knemognition.heartauth.orchestrator.interfaces.admin.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import knemognition.heartauth.orchestrator.modelaction.api.ModelActionApi;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;
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
public class AdminModelApiListener {

    private final ObjectMapper objectMapper;
    private final ModelActionApi modelActionService;

    @KafkaListener(topics = "${model.api.kafka.topics.combined}", groupId = "admin-orchestrator-group")
    public void onPredictResponse(ConsumerRecord<String, String> rec) throws Exception {
        log.info("Received Combined Predict");
        UUID id = CorrelationIdResolver.resolveOrThrow(rec);
        var dto = objectMapper.readValue(rec.value(), CombinedModelActionDto.class);
        modelActionService.save(id, dto);
    }
}
