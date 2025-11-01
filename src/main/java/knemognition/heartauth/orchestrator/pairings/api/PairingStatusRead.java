package knemognition.heartauth.orchestrator.pairings.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PairingStatusRead {
    FlowStatus status;
    String reason;
}
