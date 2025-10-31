package knemognition.heartauth.orchestrator.user.domain;


import knemognition.heartauth.orchestrator.shared.Platform;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Device {
    String deviceId;
    String displayName;
    String publicKey;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
}
