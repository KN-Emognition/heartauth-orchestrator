package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class GetChallengeStatusCmd {
    UUID tenantId;
    UUID challengeId;
}
