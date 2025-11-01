package knemognition.heartauth.orchestrator.pairings.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class EcgRefTokenClaims {

    List<List<Float>> refEcg;

}

