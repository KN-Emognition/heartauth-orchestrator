package knemognition.heartauth.orchestrator.external.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrClaims {
    private UUID jti;
    private UUID userId;
    private Instant exp;
}