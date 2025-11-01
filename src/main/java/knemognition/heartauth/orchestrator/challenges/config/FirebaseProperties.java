package knemognition.heartauth.orchestrator.challenges.config;

import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Profile(SpringProfiles.FCM)
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private String credentialsLocation;
    private boolean dryRun = false;
}