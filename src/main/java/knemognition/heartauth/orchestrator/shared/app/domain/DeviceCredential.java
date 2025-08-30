package knemognition.heartauth.orchestrator.shared.app.domain;


import com.fasterxml.jackson.databind.JsonNode;
import knemognition.heartauth.orchestrator.external.model.Platform;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCredential {
    private UUID id;
    private UUID userId;
    private String deviceId;
    private String displayName;
    private String publicKeyPem;
    private String fcmToken;
    private Platform platform;
    private String osVersion;
    private String model;
    private JsonNode attestation;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastSeenAt;
    private Instant revokedAt;
}
