package knemognition.heartauth.orchestrator.shared.app.domain;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ChallengeState {
    private String state;
    private UUID challengeId;
    private UUID userId;
    private String nonceB64;
    private Long   exp;
    private Long   createdAt;
    private String reason;
}
