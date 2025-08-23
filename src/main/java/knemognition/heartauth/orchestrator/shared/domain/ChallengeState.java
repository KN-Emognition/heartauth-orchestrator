package knemognition.heartauth.orchestrator.shared.domain;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ChallengeState {
    private String state;
    private UUID challengeId;
    private String userId;
    private String nonceB64;
    private Long   exp;
    private Long   createdAt;
    private String reason;
}
