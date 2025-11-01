package knemognition.heartauth.orchestrator.security.app.handlers;


import knemognition.heartauth.orchestrator.pairings.api.QrCodeClaims;
import knemognition.heartauth.orchestrator.security.api.InvalidTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Instant;
import java.util.UUID;

@Component
@RequestScope
public class QrClaimsHandler {

    public QrCodeClaims handle() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jat)) {
            throw new InvalidTokenException("unauthenticated");
        }
        Jwt jwt = jat.getToken();

        Instant exp = jwt.getExpiresAt();
        if (exp == null) throw new InvalidTokenException("missing_exp");
        return QrCodeClaims.builder()
                .jti(UUID.fromString(jwt.getClaim("jti")))
                .tenantId(UUID.fromString(jwt.getClaim("tenantId")))
                .userId(UUID.fromString(jwt.getClaim("userId")))
                .exp(jwt.getExpiresAt()
                        .getEpochSecond())
                .build();
    }
}
