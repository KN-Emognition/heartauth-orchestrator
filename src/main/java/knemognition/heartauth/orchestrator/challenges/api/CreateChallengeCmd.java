package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateChallengeCmd {
    UUID userId;
    UUID tenantId;
    Integer ttlSeconds;
}
