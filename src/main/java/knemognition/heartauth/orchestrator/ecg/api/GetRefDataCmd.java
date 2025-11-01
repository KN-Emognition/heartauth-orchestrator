package knemognition.heartauth.orchestrator.ecg.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class GetRefDataCmd {
    UUID userId;
}
