package knemognition.heartauth.orchestrator.external.config.rest.modelapi;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;

@Configuration
@EnableConfigurationProperties(ModelApiProperties.class)
public class ModelApiConfig {

    @Bean
    RestClient modelApiClient(ModelApiProperties p) {
        var http11 = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        var factory = new JdkClientHttpRequestFactory(http11);
        factory.setReadTimeout(p.getReadTimeout());

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(p.getBaseUrl())
                .defaultHeader(HttpHeaders.USER_AGENT, "orchestrator/1.0")
                .defaultHeaders(h -> {
                    if (p.getApiKey() != null && !p.getApiKey().isBlank()) {
                        h.add("X-API-Key", p.getApiKey());
                    }
                })
                .build();
    }
}
