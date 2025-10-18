package knemognition.heartauth.orchestrator.internal.config.rest.security;

import knemognition.heartauth.orchestrator.internal.app.ports.out.InternalMainStore;
import knemognition.heartauth.orchestrator.shared.app.ports.in.ApiKeyHasher;
import knemognition.heartauth.orchestrator.shared.constants.Authorities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class InternalApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyHasher apiKeyHasher;
    private final InternalMainStore internalMainStore;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String provided = (String) authentication.getCredentials();
        if (provided == null || provided.isBlank()) {
            throw new BadCredentialsException("invalid_api_key");
        }

        String hash = apiKeyHasher.hash(provided);

        UUID tenantId = internalMainStore.getTenantIdForActiveKeyHash(hash)
                .orElseThrow(() -> new BadCredentialsException("invalid_api_key"));


        var authorities = List.of(
                new SimpleGrantedAuthority(Authorities.TENANT)
        );
        var authed = new InternalApiKeyAuthenticationToken(tenantId, authorities);
        authed.setDetails(authentication.getDetails());
        return authed;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return InternalApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
