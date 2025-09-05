package knemognition.heartauth.orchestrator.shared.config.jwt;

import org.springframework.core.io.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String issuer,
        String audience,
        String kid,
        Integer ttlSeconds,
        Resource privateKeyLocation,
        Resource publicKeyLocation,
        String privateKeyPassword
) {
}