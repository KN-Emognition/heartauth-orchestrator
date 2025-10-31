package knemognition.heartauth.orchestrator.interfaces.external.config;


import knemognition.heartauth.orchestrator.interfaces.external.ExternalMarker;
import knemognition.heartauth.orchestrator.shared.SharedMarker;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile(SpringProfiles.EXTERNAL)
@Configuration
@ComponentScan(basePackageClasses = {ExternalMarker.class, SharedMarker.class})
public class ExternalConfig {
}