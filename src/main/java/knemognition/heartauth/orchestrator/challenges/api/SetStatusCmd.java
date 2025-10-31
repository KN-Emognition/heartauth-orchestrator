package knemognition.heartauth.orchestrator.challenges.api;

import knemognition.heartauth.orchestrator.shared.FlowStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;


@Value
@Builder
public class SetStatusCmd {
    UUID id;
    FlowStatus status;
    String reason;
}