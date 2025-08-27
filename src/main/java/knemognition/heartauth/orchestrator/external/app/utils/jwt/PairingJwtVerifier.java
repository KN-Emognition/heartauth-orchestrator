package knemognition.heartauth.orchestrator.external.app.utils.jwt;


import java.time.Instant;
import java.util.UUID;

public interface PairingJwtVerifier {
    record PairingJwtClaims(UUID jti, UUID userId, Instant exp) {}
    PairingJwtClaims verifyAndExtract(String authorizationHeader);
}
