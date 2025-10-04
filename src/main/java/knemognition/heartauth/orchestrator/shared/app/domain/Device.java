package knemognition.heartauth.orchestrator.shared.app.domain;


import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder(toBuilder = true)
public class Device {
    String deviceId;
    String displayName;
    String publicKey;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    OffsetDateTime lastSeenAt;
    OffsetDateTime revokedAt;
}
