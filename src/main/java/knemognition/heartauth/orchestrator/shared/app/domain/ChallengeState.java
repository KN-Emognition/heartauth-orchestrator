package knemognition.heartauth.orchestrator.shared.app.domain;

import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeState {
    private FlowStatus status;
    private UUID userId;
    private String nonceB64;
    private Long exp;
    private Long createdAt;
    private String reason;
    private Long ttlSeconds;
    private String privateKeyPem;
    private String userPublicKeyPem;
}