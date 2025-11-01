package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChallengeStatusRead {
    FlowStatus status;
    String reason;
}
