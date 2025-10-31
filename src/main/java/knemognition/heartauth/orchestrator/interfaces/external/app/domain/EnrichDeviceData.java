package knemognition.heartauth.orchestrator.interfaces.external.app.domain;

import knemognition.heartauth.orchestrator.shared.app.domain.Platform;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class EnrichDeviceData {
    UUID jti;
    String nonceB64;
    String deviceId;
    String displayName;
    String publicKey;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
}
