package knemognition.heartauth.orchestrator.pairings.api;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompletePairingCmd {
    String dataToken;
    String signature;
}
