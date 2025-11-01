package knemognition.heartauth.orchestrator.challenges.domain;

public enum MessageType {

    CHALLENGE("CHALLENGE");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public static MessageType fromValue(String value) {
        for (MessageType b : MessageType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

