package knemognition.heartauth.orchestrator.challenges.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CreatedChallengeResult {
    UUID id;
    Long exp;
    Long ttlSeconds;
}
