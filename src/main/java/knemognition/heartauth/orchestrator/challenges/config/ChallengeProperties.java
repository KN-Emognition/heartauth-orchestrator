package knemognition.heartauth.orchestrator.challenges.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "challenge")
public class ChallengeProperties {
    private Integer minTtl;
    private Integer defaultTtl;
    private Integer maxTtl;
    private Integer nonceLength;
}
