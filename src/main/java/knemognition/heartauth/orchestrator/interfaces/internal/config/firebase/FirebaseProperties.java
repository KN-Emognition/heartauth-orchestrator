package knemognition.heartauth.orchestrator.interfaces.internal.config.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private String credentialsLocation;
    private boolean dryRun = false;
}