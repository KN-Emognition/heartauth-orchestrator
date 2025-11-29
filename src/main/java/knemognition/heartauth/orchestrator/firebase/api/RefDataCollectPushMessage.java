package knemognition.heartauth.orchestrator.firebase.api;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefDataCollectPushMessage {
    Long ttl;
    Long exp;
}
