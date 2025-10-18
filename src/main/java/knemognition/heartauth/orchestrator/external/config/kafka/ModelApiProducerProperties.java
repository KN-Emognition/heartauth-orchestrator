package knemognition.heartauth.orchestrator.external.config.kafka;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "model.api.kafka.topics")
public class ModelApiProducerProperties {

    private String request = "ml.predict.v1";
}
