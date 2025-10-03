package knemognition.heartauth.orchestrator.internal.config.rest.security;

import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalMainStore;
import knemognition.heartauth.orchestrator.shared.app.ports.in.ApiKeyHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class InternalApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyHasher hasher;
    private final InternalMainStore apiKeyLookup;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String provided = (String) authentication.getCredentials();
        if (provided == null || provided.isBlank()) {
            throw new BadCredentialsException("invalid_api_key");
        }

        String hash = hasher.hash(provided);

        var rec = apiKeyLookup.findActiveByHash(hash)
                .orElseThrow(() -> new BadCredentialsException("invalid_api_key"));

        apiKeyLookup.updateLastUsedAt(rec.getId(), OffsetDateTime.now());

        var authorities = List.of(
                new SimpleGrantedAuthority("KEYCLOAK")
        );

        var authed = new InternalApiKeyAuthenticationToken(rec.getTenantId(), authorities);
        authed.setDetails(authentication.getDetails());
        return authed;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return InternalApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
