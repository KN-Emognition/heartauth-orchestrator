package knemognition.heartauth.orchestrator.pairings.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class GetPairingStatusCmd {
    UUID tenantId;
    UUID challengeId;
}
