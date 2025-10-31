package knemognition.heartauth.orchestrator.interfaces.internal.config.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "internal-challenge")
public class InternalChallengeProperties {
    private Integer minTtl;
    private Integer defaultTtl;
    private Integer maxTtl;
    private Integer nonceLength;
}
