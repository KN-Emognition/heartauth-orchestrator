package knemognition.heartauth.orchestrator.challenges.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;


@Value
@Builder
public class EcgTestTokenClaims {
    List<Float> testEcg;
}

