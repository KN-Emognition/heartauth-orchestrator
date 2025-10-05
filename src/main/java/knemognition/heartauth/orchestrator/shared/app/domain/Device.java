package knemognition.heartauth.orchestrator.shared.app.domain;


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
