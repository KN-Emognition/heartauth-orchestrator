package knemognition.heartauth.orchestrator.api.kafka;

import knemognition.heartauth.orchestrator.challenges.api.ChallengesModule;
import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.shared.gateways.kafka.modelapi.model.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile(SpringProfiles.INTERNAL)
public class InternalKafka {

    private final DtoMapper dtoMapper;
    private final ChallengesModule challengesModule;

    @KafkaListener(topics = "${model.api.kafka.topics.reply}")
    public void handlePredict(
            @Payload PredictResponseDto payload,
            @Header(HeaderNames.HEADER_MODEL_API_UNIQUE_ID) String uniqueModelId,
            @Header(name = KafkaHeaders.KEY, required = false) String correlationId
    ) {
        log.info("[INTERNAL-KAFKA] Received model API prediction response | key={}", correlationId);
        var cmd = dtoMapper.toCmd(payload, UUID.fromString(uniqueModelId));
        challengesModule.complete(cmd);
    }
}
