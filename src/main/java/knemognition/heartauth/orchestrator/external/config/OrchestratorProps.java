package knemognition.heartauth.orchestrator.external.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "orchestrator")
public class OrchestratorProps {

    private int maxDevicesPerUser = 3;

    @Data
    public static class PairingProps {
        private int nonceBytes = 32;
        private long maxTtlSeconds = 300;
        private long linkedKeepTtlSeconds = 60;
    }

    private PairingProps pairing = new PairingProps();
}
