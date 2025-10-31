package knemognition.heartauth.orchestrator.challenges.api;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;


/**
 * Result object representing the outcome of creating a new flow.
 */
@Value
@Builder
public class CreatedChallenge {
    UUID id;

    Long exp;
    Long ttlSeconds;
}