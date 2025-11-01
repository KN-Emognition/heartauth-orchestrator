package knemognition.heartauth.orchestrator.security.app.impl.internal;

import knemognition.heartauth.orchestrator.security.api.SecurityModule;
import knemognition.heartauth.orchestrator.shared.constants.Authorities;
import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
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

    private final SecurityModule apiKeyHasher;
    private final TenantsModule tenantsModule;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String provided = (String) authentication.getCredentials();
        if (provided == null || provided.isBlank()) {
            throw new BadCredentialsException("invalid_api_key");
        }

        String hash = apiKeyHasher.hash(provided);

        UUID tenantId = tenantsModule.getByApiKey(hash)
                .map(TenantRead::getTenantId)
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
