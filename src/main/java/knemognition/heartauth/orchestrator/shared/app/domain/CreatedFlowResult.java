package knemognition.heartauth.orchestrator.shared.app.domain;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatedFlowResult {
    private UUID id;
    private Long exp;
    private Long ttl;
}