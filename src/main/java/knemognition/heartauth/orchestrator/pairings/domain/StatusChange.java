package knemognition.heartauth.orchestrator.pairings.domain;

import knemognition.heartauth.orchestrator.pairings.api.FlowStatus;
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