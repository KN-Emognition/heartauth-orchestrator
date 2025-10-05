package knemognition.heartauth.orchestrator.external.gateways.rest.modelapi.api;

import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApi;
import knemognition.heartauth.orchestrator.external.gateways.rest.modelapi.mapper.ModelApiMapper;
import knemognition.heartauth.orchestrator.shared.app.domain.EcgPrediction;
import knemognition.heartauth.orchestrator.shared.gateways.rest.modelapi.api.PredictionApi;
import knemognition.heartauth.orchestrator.shared.gateways.rest.modelapi.model.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelApiImpl implements ModelApi {
    private final ModelApiMapper modelApiMapper;
    private final PredictionApi predictionApi;

    @Override
    public boolean predict(EcgPrediction prediction) {
        try {
            PredictResponseDto response = predictionApi.predict(modelApiMapper.toPredictRequestDto(prediction));
            return response.getPrediction();
        } catch (Exception e) {
            log.error("Error while calling model api", e);
            throw new RuntimeException("Error while calling model api", e);
        }
    }
}
