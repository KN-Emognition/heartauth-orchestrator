package knemognition.heartauth.orchestrator;

import knemognition.heartauth.orchestrator.interfaces.admin.config.AdminConfig;
import knemognition.heartauth.orchestrator.interfaces.external.config.ExternalConfig;
import knemognition.heartauth.orchestrator.interfaces.internal.config.InternalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.Modulithic;


@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "knemognition.heartauth.orchestrator.modules")
@Modulithic
@Import({ExternalConfig.class, InternalConfig.class, AdminConfig.class})
public class OrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }

}

