package knemognition.heartauth.orchestrator.shared.config.kafka;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "model.api.kafka.topics")
public class ModelApiTopicsProperties {
    private String request;
    private String reply;
    private String combined;
}
