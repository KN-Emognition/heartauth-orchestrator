package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    private String type;
    private UUID challengeId;
    private String nonce;
    private Long ttl;
    private Long exp;
}
