package knemognition.heartauth.orchestrator.external.config.qr;


import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.InvalidTokenException;
import knemognition.heartauth.orchestrator.shared.app.domain.QrCodeClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Instant;

@Component
@RequestScope
public class QrClaimsProvider {

    public QrCodeClaims get() {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jat)) {
            throw new InvalidTokenException("unauthenticated");
        }
        Jwt jwt = jat.getToken();

        Instant exp = jwt.getExpiresAt();
        if (exp == null) throw new InvalidTokenException("missing_exp");
        return QrCodeClaims.builder()
                .jti(jwt.getClaim("jti"))
                .tenantId(jwt.getClaim("tenantId"))
                .userId(jwt.getClaim("userId"))
                .exp(jwt.getClaim("exp"))
                .build();
//                UUID.fromString(jwt.getId()),
//                UUID.fromString(jwt.getSubject()),
//                exp

    }
}
