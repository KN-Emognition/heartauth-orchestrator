package knemognition.heartauth.orchestrator.shared.app.domain;


import knemognition.heartauth.orchestrator.internal.model.FlowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowStatusDescription {
    FlowStatus status;
    String reason;
}
