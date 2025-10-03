package knemognition.heartauth.orchestrator.shared.app.domain;


import lombok.*;

@Value
@Builder
public class FlowStatusDescription {
    FlowStatus status;
    String reason;
}
