package knemognition.heartauth.orchestrator.ecg.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RefEcg {
    List<List<Float>> refEcg;
}


