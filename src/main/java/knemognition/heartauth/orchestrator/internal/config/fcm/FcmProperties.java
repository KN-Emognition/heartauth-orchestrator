package knemognition.heartauth.orchestrator.internal.config.fcm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "fcm")
public class FcmProperties {
    private String projectId;
    private String credentialsPath;
    private boolean dryRun = false;
}
