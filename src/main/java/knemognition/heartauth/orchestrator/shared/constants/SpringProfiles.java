package knemognition.heartauth.orchestrator.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.modulith.NamedInterface;

@NamedInterface
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringProfiles {
    public static final String TENANT = "tenant";
    public static final String MOBILE = "mobile";
    public static final String ADMIN = "admin";
    public static final String FCM = "fcm";
    public static final String E2E = "e2e";
}
