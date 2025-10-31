package knemognition.heartauth.orchestrator.user.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class EcgRefData {
    List<List<Float>> refEcg;
}
