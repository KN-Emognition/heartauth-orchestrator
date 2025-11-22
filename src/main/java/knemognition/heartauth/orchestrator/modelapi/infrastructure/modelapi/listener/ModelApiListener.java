package knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.listener;

import knemognition.heartauth.orchestrator.modelapi.api.ModelApiCallbackApi;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.CombinedModelActionDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.kafka.model.PredictResponseDto;
import knemognition.heartauth.orchestrator.modelapi.infrastructure.modelapi.mapper.ModelApiKafkaMapper;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
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
@Profile(SpringProfiles.TENANT)
public class ModelApiListener {

    private final ModelApiKafkaMapper dtoMapper;
    private final ModelApiCallbackApi modelApiCallbackApi;

    @KafkaListener(topics = "${model.api.topics.reply}")
    public void handlePredict(
            @Payload PredictResponseDto payload,
            @Header(HeaderNames.HEADER_MODEL_API_UNIQUE_ID) String uniqueModelId,
            @Header(name = KafkaHeaders.KEY, required = false) String correlationId
    ) {
        log.info("[MODELAPI] Received model API prediction response");
        var cmd = dtoMapper.toCmd(payload, UUID.fromString(uniqueModelId));
        modelApiCallbackApi.handle(cmd);
    }

    @KafkaListener(topics = "${model.api.topics.combined}", groupId = "admin-orchestrator-group")
    public void onCombined(@Payload CombinedModelActionDto payload,
                           @Header(HeaderNames.HEADER_CORRELATION_ID) String correlationId,
                           @Header(name = KafkaHeaders.KEY, required = false) String key) {
        log.info("[MODELAPI] Received Combined Predict");
    }
}
