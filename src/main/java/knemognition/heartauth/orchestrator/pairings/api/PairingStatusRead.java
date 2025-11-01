package knemognition.heartauth.orchestrator.pairings.api;

import knemognition.heartauth.orchestrator.shared.app.domain.FlowStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PairingStatusRead {
    FlowStatus status;
    String reason;
}
