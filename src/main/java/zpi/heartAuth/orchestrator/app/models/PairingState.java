package zpi.heartAuth.orchestrator.app.models;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PairingState {
    private final UUID jti;
    private final String userId;
    private final String deviceId;
    private final String displayName;
    private final String publicKeyPem;
    private final String fcmToken;
    private final String attestationType;     // "play-integrity" | "devicecheck" | "none"
    private final String attestationVerdict;  // nullable
    private final byte[] nonce;               // 32 bytes
    private final Instant expiresAt;
    @Setter
    private PairingStatus status;
}
