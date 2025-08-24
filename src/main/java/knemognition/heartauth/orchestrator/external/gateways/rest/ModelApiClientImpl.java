package knemognition.heartauth.orchestrator.external.gateways.rest;


import knemognition.heartauth.orchestrator.client.modelapi.In;
import knemognition.heartauth.orchestrator.client.modelapi.PredictResponse;
import knemognition.heartauth.orchestrator.external.app.ports.out.ModelApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
@RequiredArgsConstructor
public class ModelApiClientImpl implements ModelApiClient {

    private final RestClient modelApiRestClient;

    public PredictResponse predict(In in) {
        return modelApiRestClient.post()
                .uri("/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(in)
                .retrieve()
                .body(PredictResponse.class);
    }
}
