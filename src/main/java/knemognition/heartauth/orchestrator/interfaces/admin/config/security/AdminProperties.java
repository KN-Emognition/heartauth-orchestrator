package knemognition.heartauth.orchestrator.interfaces.admin.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {
    private String apiKey;
}
