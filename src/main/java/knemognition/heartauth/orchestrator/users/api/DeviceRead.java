package knemognition.heartauth.orchestrator.users.api;


import knemognition.heartauth.orchestrator.shared.app.domain.Platform;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeviceRead {
    String deviceId;
    String displayName;
    String publicKey;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
}
