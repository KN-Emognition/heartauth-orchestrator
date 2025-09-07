package knemognition.heartauth.orchestrator.external.config.qr;


import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.InvalidTokenException;
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
public class QrClaimsProvider {

    public QrClaims get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jat)) {
            throw new InvalidTokenException("unauthenticated");
        }
        Jwt jwt = jat.getToken();

        Instant exp = jwt.getExpiresAt();
        if (exp == null) throw new InvalidTokenException("missing_exp");
        return new QrClaims(
                UUID.fromString(jwt.getId()),
                UUID.fromString(jwt.getSubject()),
                exp
        );
    }
}
