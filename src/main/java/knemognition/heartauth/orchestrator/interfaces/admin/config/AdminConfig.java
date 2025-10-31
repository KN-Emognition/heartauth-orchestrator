package knemognition.heartauth.orchestrator.interfaces.admin.config;


import knemognition.heartauth.orchestrator.interfaces.admin.AdminService;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile(SpringProfiles.ADMIN)
@Configuration
@ComponentScan(basePackageClasses = {AdminService.class})
public class AdminConfig {
}