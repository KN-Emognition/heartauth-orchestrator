package knemognition.heartauth.orchestrator.shared.config.jwt;

import org.springframework.core.io.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String kid,
        Resource privateKeyLocation,
        Resource publicKeyLocation,
        String privateKeyPassword
) {
}