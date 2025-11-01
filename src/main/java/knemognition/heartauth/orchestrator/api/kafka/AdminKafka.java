package knemognition.heartauth.orchestrator.api.kafka;

import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.CombinedModelActionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile(SpringProfiles.ADMIN)
public class AdminKafka {

    @KafkaListener(topics = "${model.api.kafka.topics.combined}", groupId = "admin-orchestrator-group")
    public void onPredictResponse(@Payload CombinedModelActionDto payload,
                                  @Header(HeaderNames.HEADER_CORRELATION_ID) String correlationId,
                                  @Header(name = KafkaHeaders.KEY, required = false) String key) {
        log.info("[ADMIN-KAFKA] Received Combined Predict");
    }
}
