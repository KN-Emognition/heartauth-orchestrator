package knemognition.heartauth.orchestrator.shared.security;

import org.springframework.core.io.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pairing-jwt")
public record PairingJwtProperties(
        String issuer,
        String audience,
        String kid,
        Integer ttlSeconds,
        Resource privateKeyLocation,
        Resource publicKeyLocation,
        String privateKeyPassword
) {
}