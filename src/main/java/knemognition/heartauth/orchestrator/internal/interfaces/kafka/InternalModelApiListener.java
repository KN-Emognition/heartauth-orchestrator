package knemognition.heartauth.orchestrator.internal.interfaces.kafka;

import knemognition.heartauth.orchestrator.internal.app.ports.in.InternalChallengeService;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalModelApiListener {

    private final InternalChallengeService internalChallengeService;

    @KafkaListener(topics = "${model.api.kafka.topics.reply}")
    public void handlePredict(
            @Payload PredictResponseDto payload,
            @Header(HeaderNames.HEADER_MODEL_API_UNIQUE_ID) String uniqueModelId,
            @Header(name = KafkaHeaders.KEY, required = false) String correlationId
    ) {
        log.info("Received model API prediction response | key={}", correlationId);
        internalChallengeService.completeChallengeWithPrediction(UUID.fromString(uniqueModelId), payload);
    }
}
