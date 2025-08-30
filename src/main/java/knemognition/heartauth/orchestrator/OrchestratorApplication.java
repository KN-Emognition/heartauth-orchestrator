package knemognition.heartauth.orchestrator;

import knemognition.heartauth.orchestrator.external.config.ExternalConfig;
import knemognition.heartauth.orchestrator.internal.config.InternalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "knemognition.heartauth.orchestrator.shared")
@Import({ ExternalConfig.class, InternalConfig.class })
public class OrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }

}

