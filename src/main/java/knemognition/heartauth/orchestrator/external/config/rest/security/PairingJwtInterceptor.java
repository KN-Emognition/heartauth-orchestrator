package knemognition.heartauth.orchestrator.external.config.rest.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.config.errorhandling.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PairingJwtInterceptor implements HandlerInterceptor {
    public static final String REQ_ATTR_QR_CLAIMS = "qrClaims";
    private final ObjectFactory<HttpServletRequest> requestFactory;
    private static final Duration ALLOWED_SKEW = Duration.ofSeconds(30);

    private final NimbusJwtDecoder pairingJwtDecoder;
    private final Clock clock = Clock.systemUTC();
    private static final Set<String> TARGET_METHODS =
            Set.of("externalPairingConfirm", "externalPairingInit");

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if (handler instanceof HandlerMethod hm && TARGET_METHODS.contains(hm.getMethod().getName())) {
            QrClaims claims = process();
            req.setAttribute(REQ_ATTR_QR_CLAIMS, claims);
        }
        return true;
    }


    private QrClaims process() {
        HttpServletRequest request = requestFactory.getObject();
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank()) {
            throw new InvalidTokenException("missing_authorization");
        }

        final String token = extractBearer(auth);

        final Jwt jwt;
        try {
            jwt = pairingJwtDecoder.decode(token);
        } catch (BadJwtException e) {
            throw new InvalidTokenException("invalid_token_signature");
        } catch (JwtException e) {
            throw new InvalidTokenException("invalid_token");
        }

        UUID jti = UUID.fromString(jwt.getId());
        UUID userId = UUID.fromString(jwt.getSubject());
        Instant exp = jwt.getExpiresAt();
        Instant now = Instant.now(clock);
        if (exp.plus(ALLOWED_SKEW).isBefore(now)) {
            throw new InvalidTokenException("pairing_token_expired");
        }
        QrClaims claims = new QrClaims(jti, userId, exp);
        log.info("Verified Token and extracted Claims");
        return claims;
    }

    private static String extractBearer(String header) {
        if (header == null || header.isBlank()) throw new InvalidTokenException("missing_authorization");
        final String prefix = "Bearer ";
        if (!header.regionMatches(true, 0, prefix, 0, prefix.length())) {
            throw new InvalidTokenException("invalid_authorization_scheme");
        }
        return header.substring(prefix.length()).trim();
    }
}