package knemognition.heartauth.orchestrator.pairings.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class QrCodeClaims {
    UUID userId;
    UUID tenantId;
    Long exp;
    UUID jti;
}

