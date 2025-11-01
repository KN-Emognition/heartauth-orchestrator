package knemognition.heartauth.orchestrator.pairings.api;

import knemognition.heartauth.orchestrator.shared.app.domain.Platform;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InitPairingCmd {
    String deviceId;
    String displayName;
    String publicKey;
    String fcmToken;
    Platform platform;
    String osVersion;
    String model;
}
