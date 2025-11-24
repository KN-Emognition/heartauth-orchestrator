package knemognition.heartauth.orchestrator.pairings.api;


import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreatePairingCmd {
    UUID tenantId;
    UUID userId;
    String username;
    Integer ttlSeconds;
}
