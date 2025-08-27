package knemognition.heartauth.orchestrator.external.config;


import knemognition.heartauth.orchestrator.external.ExternalMarker;
import knemognition.heartauth.orchestrator.shared.SharedMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile({"external" ,"orchestrator"})
@Configuration
@ComponentScan(basePackageClasses = { ExternalMarker.class, SharedMarker.class })
public class ExternalConfig {}