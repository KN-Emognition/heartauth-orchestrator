package knemognition.heartauth.orchestrator.shared.app.domain;


import knemognition.heartauth.orchestrator.external.model.Platform;
import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PairingState {
    private UUID userId;
    private String deviceId;
    private String displayName;
    private String publicKeyPem;
    private String fcmToken;
    private Platform platform;
    private String osVersion;
    private String model;

    private String attestationType;
    private String attestationVerdict;
    private String attestationPayloadJson;

    private FlowStatus status;
    private String reason;

    private String nonceB64;
    private Long exp;
    private Long createdAt;
    private Long ttlSeconds;
}

