package knemognition.heartauth.orchestrator.pairings.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;


/**
 * Result object representing the outcome of creating a new flow.
 */
@Value
@Builder
public class CreatedPairingResult {
    UUID id;
    Long exp;
    Long ttlSeconds;
}