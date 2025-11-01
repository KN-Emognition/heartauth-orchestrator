package knemognition.heartauth.orchestrator.challenges.api;

import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChallengeStatusRead {
    FlowStatus status;
    String reason;
}
