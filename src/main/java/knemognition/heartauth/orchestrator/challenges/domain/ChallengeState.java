package knemognition.heartauth.orchestrator.challenges.domain;

import knemognition.heartauth.orchestrator.shared.FlowStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ChallengeState {

    UUID userId;
    UUID tenantId;
    UUID id;
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

