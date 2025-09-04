package knemognition.heartauth.orchestrator.shared.app.domain;

import knemognition.heartauth.orchestrator.external.model.Attestation;
import knemognition.heartauth.orchestrator.external.model.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrichDeviceData {
    private UUID jti;

    private String nonceB64;

    private String deviceId;

    private String displayName;

    private String publicKeyPem;

    private String fcmToken;

    private Platform platform;

    private String osVersion;

    private String model;

    private Attestation attestation;
}
