package knemognition.heartauth.orchestrator.users.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {

    ANDROID("ANDROID"),
    IOS("IOS");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
