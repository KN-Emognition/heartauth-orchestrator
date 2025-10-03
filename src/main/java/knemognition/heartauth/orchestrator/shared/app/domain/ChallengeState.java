package knemognition.heartauth.orchestrator.shared.app.domain;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ChallengeState {
    UUID userId;
    UUID tenantId;

    FlowStatus status;
    String reason;

    String ephemeralPrivateKey;
    String userPublicKey;
    String nonceB64;

    Long exp;
    Long createdAt;
    Long ttlSeconds;
}