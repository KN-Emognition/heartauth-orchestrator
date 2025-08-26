package knemognition.heartauth.orchestrator.shared.security;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "security.api")
public record ApiKeyProperties(
        @NotEmpty List<ApiKey> keys
) {
    public record ApiKey(
            @NotBlank String id,
            @NotBlank String value,
            List<@NotBlank String> roles
    ) {
        public ApiKey {
            roles = (roles == null || roles.isEmpty()) ? List.of("API_CLIENT") : List.copyOf(roles);
        }

        public boolean matches(String provided) {
            if (provided == null) return false;
            return MessageDigest.isEqual(
                    value.getBytes(StandardCharsets.UTF_8),
                    provided.getBytes(StandardCharsets.UTF_8));
        }
    }

    public ApiKeyProperties {
        keys = List.copyOf(keys);
    }
}
