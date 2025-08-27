package knemognition.heartauth.orchestrator.external.app.utils.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PairingJwtVerifierImpl implements PairingJwtVerifier {

    private final JwtDecoder pairingJwtDecoder;

    @Override
    public PairingJwtClaims verifyAndExtract(String authorizationHeader) {
        String token = extractBearer(authorizationHeader);

        Jwt jwt = pairingJwtDecoder.decode(token);

        // jti
        String jtiStr = jwt.getId();
        if (jtiStr == null) throw new IllegalArgumentException("missing_jti");
        UUID jti = parseUuid(jtiStr, "jti");

        // userId: prefer 'sub', fallback to 'user_id'
        String sub = jwt.getSubject();
        if (sub == null) sub = jwt.getClaimAsString("user_id");
        if (sub == null) throw new IllegalArgumentException("missing_user_id");
        UUID userId = parseUuid(sub, "userId");

        Instant exp = jwt.getExpiresAt();
        if (exp == null) throw new IllegalArgumentException("missing_exp");

        return new PairingJwtClaims(jti, userId, exp);
    }

    private static String extractBearer(String header) {
        if (header == null || header.isBlank()) throw new IllegalArgumentException("missing_authorization");
        String prefix = "Bearer ";
        if (!header.regionMatches(true, 0, prefix, 0, prefix.length())) {
            throw new IllegalArgumentException("invalid_authorization_scheme");
        }
        return header.substring(prefix.length()).trim();
    }

    private static UUID parseUuid(String val, String field) {
        try {
            return UUID.fromString(val);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid_" + field + "_uuid");
        }
    }
}
