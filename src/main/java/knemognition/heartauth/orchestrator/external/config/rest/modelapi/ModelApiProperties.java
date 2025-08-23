package knemognition.heartauth.orchestrator.external.config.rest.modelapi;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "model.api")
@Getter
@Setter
public class ModelApiProperties {
    private String baseUrl;
    private String apiKey;
    private Duration connectTimeout = Duration.ofSeconds(3);
    private Duration readTimeout = Duration.ofSeconds(5);
    private Duration writeTimeout = Duration.ofSeconds(5);
}
