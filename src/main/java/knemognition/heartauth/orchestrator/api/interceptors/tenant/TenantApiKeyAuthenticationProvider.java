package knemognition.heartauth.orchestrator.api.interceptors.tenant;

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
public class TenantApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final TenantsModule tenantsModule;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String provided = (String) authentication.getCredentials();
        if (provided == null || provided.isBlank()) {
            throw new BadCredentialsException("invalid_api_key");
        }

        UUID tenantId = tenantsModule.getByApiKey(provided)
                .map(TenantRead::getTenantId)
                .orElseThrow(() -> new BadCredentialsException("invalid_api_key"));


        var authorities = List.of(
                new SimpleGrantedAuthority(Authorities.TENANT)
        );
        var authed = new TenantApiKeyAuthenticationToken(tenantId, authorities);
        authed.setDetails(authentication.getDetails());
        return authed;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TenantApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
