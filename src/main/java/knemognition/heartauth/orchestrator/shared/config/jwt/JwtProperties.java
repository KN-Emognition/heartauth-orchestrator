package knemognition.heartauth.orchestrator.shared.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String kid,
        Resource privateKeyLocation,
        Resource publicKeyLocation,
        String privateKeyPassword
) {
}