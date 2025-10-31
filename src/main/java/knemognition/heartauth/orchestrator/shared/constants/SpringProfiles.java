package knemognition.heartauth.orchestrator.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.modulith.NamedInterface;

@NamedInterface
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringProfiles {
    public static final String DEV = "dev";
    public static final String INTERNAL = "internal";
    public static final String EXTERNAL = "external";
    public static final String ADMIN = "admin";
    public static final String FCM_MOCK = "fcm-mock";
    public static final String E2E = "e2e";
}
