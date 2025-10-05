package knemognition.heartauth.orchestrator.shared.app.domain;

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