package knemognition.heartauth.orchestrator.external.app.domain;

import java.time.Instant;
import java.util.UUID;

public record QrClaims(UUID jti, UUID userId, Instant exp) {
}