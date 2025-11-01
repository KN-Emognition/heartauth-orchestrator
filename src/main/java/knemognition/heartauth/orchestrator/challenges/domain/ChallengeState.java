package knemognition.heartauth.orchestrator.challenges.domain;

import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;


@Value
@Builder
public class ChallengeState {
    UUID id;
    UUID userId;
    UUID tenantId;

    FlowStatus status;
    String reason;
    String ephemeralPrivateKey;
    String userPublicKey;
    String nonceB64;
    UUID modelApiTryId;

    Long exp;
    Long createdAt;
    Long ttlSeconds;
}

