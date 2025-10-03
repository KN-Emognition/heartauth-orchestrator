package knemognition.heartauth.orchestrator.shared.app.domain;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusChange {
    private UUID id;
    private FlowStatus status;
    private String reason;
}