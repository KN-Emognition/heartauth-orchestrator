package knemognition.heartauth.orchestrator.external.app.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcgRefToken {
    private List<List<Float>> refEcg;
    private UUID userId;
}