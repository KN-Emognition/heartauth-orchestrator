package knemognition.heartauth.orchestrator.shared.app.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class EcgRefData {
    List<List<Float>> refEcg;
}
