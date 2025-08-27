package knemognition.heartauth.orchestrator.external.app.utils;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.external.app.utils.jwt.PairingJwtVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PairingJwtClaimsProvider {
    public record Claims(UUID jti, UUID userId, Instant exp) {
    }

    private final PairingJwtVerifier verifier;
    private final HttpServletRequest request;

    public Claims getClaimsOrThrow() {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank()) {
            throw new IllegalArgumentException("missing_authorization");
        }
        var c = verifier.verifyAndExtract(auth);
        return new Claims(c.jti(), c.userId(), c.exp());
    }
}

