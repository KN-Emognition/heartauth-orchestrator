package knemognition.heartauth.orchestrator.internal.config.pairing;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "internal-pairing")
public class InternalPairingProperties {
    private Integer minTtl;
    private Integer defaultTtl;
    private Integer maxTtl;
    private String issuer;
    private List<@NotBlank String> audience;
}
