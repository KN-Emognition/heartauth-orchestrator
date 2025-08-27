package knemognition.heartauth.orchestrator.external.app.utils.jwt;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pairing.jwt")
public class PairingJwtProps {
    /**
     * OIDC issuer (e.g. https://keycloak.example.com/realms/myrealm)
     */
    private String issuer;
    /**
     * Expected audience (e.g. the client-id that issues the pairing token)
     */
    private String audience;
}
