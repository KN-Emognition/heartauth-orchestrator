package knemognition.heartauth.orchestrator.shared.app.domain;


import knemognition.heartauth.orchestrator.external.model.Platform;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Device {
    UUID id;
    UUID appUserId;
    String deviceId;
    String displayName;
    String publicKeyPem;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    OffsetDateTime lastSeenAt;
    OffsetDateTime revokedAt;
}
