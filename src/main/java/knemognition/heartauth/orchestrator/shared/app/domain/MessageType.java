package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Type of push message.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public enum MessageType {
  
  CHALLENGE("CHALLENGE");

  private final String value;

  MessageType(String value) {
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
  public static MessageType fromValue(String value) {
    for (MessageType b : MessageType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

