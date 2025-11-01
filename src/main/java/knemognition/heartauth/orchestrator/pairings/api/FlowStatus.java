package knemognition.heartauth.orchestrator.pairings.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


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

