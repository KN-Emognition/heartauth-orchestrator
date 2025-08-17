package zpi.heartAuth.orchestrator.app.models;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class DeviceCredential {
    private final String userId;
    private final String deviceId;
    private final String displayName;
    private final String publicKeyPem;
    private final String fcmToken;
    private final String attestationType;
    private final String attestationVerdict;
    private final Instant createdAt;
}
