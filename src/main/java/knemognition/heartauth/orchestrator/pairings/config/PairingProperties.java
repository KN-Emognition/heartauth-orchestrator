package knemognition.heartauth.orchestrator.pairings.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.modulith.NamedInterface;

import java.util.List;

@NamedInterface
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "pairing")
public class PairingProperties {
    private Integer minTtl;
    private Integer defaultTtl;
    private Integer nonceLength;
    private Integer maxTtl;
    private String issuer;
    private List<@NotBlank String> audience;
}
