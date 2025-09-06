package knemognition.heartauth.orchestrator.external.config.rest.modelapi;

import knemognition.heartauth.orchestrator.ApiClient;
import knemognition.heartauth.orchestrator.modelapi.api.PredictionApi;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

import static knemognition.heartauth.orchestrator.shared.config.mdc.HeaderNames.*;


@Configuration
@EnableConfigurationProperties(ModelApiProperties.class)
public class ModelApiConfig {


    @Bean
    public ApiClient apiClient(RestClient.Builder builder, ModelApiProperties p) {
        RestClient restClient = builder
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    request.getHeaders().setAccept(List.of(MediaType.APPLICATION_JSON));
                    String routeId = MDC.get(MDC_ROUTE_ID);
                    if (routeId != null && !routeId.isBlank()) {
                        request.getHeaders().set(HEADER_ROUTE_ID, routeId);
                    }
                    return execution.execute(request, body);
                })
                .build();


        ApiClient apiClient = new ApiClient(restClient);
        apiClient.setBasePath(p.getBaseUrl());
        apiClient.addDefaultHeader(HEADER_API_KEY, p.getApiKey());

        return apiClient;
    }

    @Bean
    public PredictionApi predictionApi(ApiClient apiClient) {
        return new PredictionApi(apiClient);
    }
}
