package knemognition.heartauth.orchestrator.shared.app.domain;


import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.*;

@Value
@Builder
public class FlowStatusDescription {
    FlowStatus status;
    String reason;
}
