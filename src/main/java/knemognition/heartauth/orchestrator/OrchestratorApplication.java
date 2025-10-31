package knemognition.heartauth.orchestrator;

import knemognition.heartauth.orchestrator.admin.config.AdminConfig;
import knemognition.heartauth.orchestrator.external.config.ExternalConfig;
import knemognition.heartauth.orchestrator.internal.config.InternalConfig;
import knemognition.heartauth.orchestrator.security.SecurityModule;
import knemognition.heartauth.orchestrator.shared.SharedMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.Modulithic;


@SpringBootApplication
@ComponentScan(basePackageClasses = {SharedMarker.class, SecurityModule.class})
@ConfigurationPropertiesScan(basePackages = "knemognition.heartauth.orchestrator.shared")
@Modulithic
@Import({ExternalConfig.class, InternalConfig.class, AdminConfig.class})
public class OrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }

}

