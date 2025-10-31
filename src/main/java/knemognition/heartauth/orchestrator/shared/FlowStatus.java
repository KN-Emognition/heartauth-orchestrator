package knemognition.heartauth.orchestrator.shared;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Lifecycle state of a pairing/challenge flow.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public enum FlowStatus {
  
  CREATED("CREATED"),
  
  PENDING("PENDING"),
  
  APPROVED("APPROVED"),
  
  DENIED("DENIED"),
  
  EXPIRED("EXPIRED"),
  
  NOT_FOUND("NOT_FOUND");

  private final String value;

  FlowStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static FlowStatus fromValue(String value) {
    for (FlowStatus b : FlowStatus.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

