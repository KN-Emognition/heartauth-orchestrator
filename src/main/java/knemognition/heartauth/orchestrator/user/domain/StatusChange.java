package knemognition.heartauth.orchestrator.user.domain;

import knemognition.heartauth.orchestrator.shared.FlowStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;


@Value
@Builder
public class StatusChange {
    UUID id;
    FlowStatus status;
    String reason;
}