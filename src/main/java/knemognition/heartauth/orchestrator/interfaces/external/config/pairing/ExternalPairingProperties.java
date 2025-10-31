package knemognition.heartauth.orchestrator.interfaces.external.config.pairing;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "external-pairing")
public class ExternalPairingProperties {
    private Integer nonceLength;
    private String issuer;
    private List<@NotBlank String> audience;
}
