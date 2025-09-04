package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePairing {
    private UUID userId;
    private UUID jti;
    private Long ttlSeconds;
}