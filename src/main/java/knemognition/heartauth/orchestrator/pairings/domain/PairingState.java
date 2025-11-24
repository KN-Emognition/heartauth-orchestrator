package knemognition.heartauth.orchestrator.pairings.domain;

import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
import knemognition.heartauth.orchestrator.users.api.Platform;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PairingState {
    UUID id;
    UUID userId;
    UUID tenantId;
    FlowStatus status;
    String username;
    String reason;
    String deviceId;
    String displayName;
    String publicKey;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
    String nonceB64;
    Long exp;
    Long createdAt;
    Long ttlSeconds;
}

