package knemognition.heartauth.orchestrator.admin.config;


import knemognition.heartauth.orchestrator.admin.AdminMarker;
import knemognition.heartauth.orchestrator.shared.SharedMarker;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile(SpringProfiles.ADMIN)
@Configuration
@ComponentScan(basePackageClasses = {AdminMarker.class, SharedMarker.class})
public class AdminConfig {
}