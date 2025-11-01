package knemognition.heartauth.orchestrator.challenges.domain;

import knemognition.heartauth.orchestrator.challenges.api.FlowStatus;
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