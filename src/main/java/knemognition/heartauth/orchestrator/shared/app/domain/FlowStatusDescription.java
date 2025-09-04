package knemognition.heartauth.orchestrator.shared.app.domain;


import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowStatusDescription {
    FlowStatus status;
    String reason;
}
