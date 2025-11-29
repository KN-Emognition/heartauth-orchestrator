package knemognition.heartauth.orchestrator.refdatacollect.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreateRefDataCollectNotificationCmd {
    UUID processId;
}
