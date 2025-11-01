package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class CreatedChallengeRead {
    UUID challengeId;
}
