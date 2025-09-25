package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateChallenge {
    private UUID userId;
    private String nonceB64;
    private Long ttlSeconds;
    private String privateKey;
    private String userPublicKey;
}