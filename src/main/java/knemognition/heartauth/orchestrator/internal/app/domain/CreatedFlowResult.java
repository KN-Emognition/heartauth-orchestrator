package knemognition.heartauth.orchestrator.internal.app.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;


/**
 * Result object representing the outcome of creating a new flow.
 */
@Value
@Builder
public class CreatedFlowResult {
    UUID id;

    Long exp;
    Long ttlSeconds;
}