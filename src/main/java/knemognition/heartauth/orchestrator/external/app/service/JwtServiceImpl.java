package knemognition.heartauth.orchestrator.external.app.service;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.ports.in.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final HttpServletRequest request;
    private static final Duration ALLOWED_SKEW = Duration.ofSeconds(30);

    private final JwtDecoder pairingJwtDecoder;
    private final Clock clock = Clock.systemUTC();

    @Override
    public QrClaims process() {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank()) {
            throw new IllegalArgumentException("missing_authorization");
        }

        final String token = extractBearer(auth);

        final Jwt jwt;
        try {
            jwt = pairingJwtDecoder.decode(token);
        } catch (BadJwtException e) {
            debug("JWT rejected by decoder: {}", e.getMessage());
            throw new IllegalArgumentException("invalid_token_signature");
        } catch (JwtException e) {
            debug("JWT decode error: {}", e.getMessage());
            throw new IllegalArgumentException("invalid_token");
        }

        UUID jti = UUID.fromString(jwt.getId());
        UUID userId = UUID.fromString(jwt.getSubject());
        Instant exp = jwt.getExpiresAt();

        Instant now = Instant.now(clock);
        if (exp.plus(ALLOWED_SKEW).isBefore(now)) {
            throw new IllegalArgumentException("pairing_token_expired");
        }
        QrClaims claims = new QrClaims(jti, userId, exp);
        log.info("Verified Token and extracted Claims");
        return claims;
    }

    private static String extractBearer(String header) {
        if (header == null || header.isBlank()) throw new IllegalArgumentException("missing_authorization");
        final String prefix = "Bearer ";
        if (!header.regionMatches(true, 0, prefix, 0, prefix.length())) {
            throw new IllegalArgumentException("invalid_authorization_scheme");
        }
        return header.substring(prefix.length()).trim();
    }

    private static void debug(String msg, Object arg) {
        String rid = MDC.get("routeId");
        if (rid != null) {
            log.debug("[routeId={}] " + msg, rid, arg);
        } else {
            log.debug(msg, arg);
        }
    }
}

