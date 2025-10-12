package knemognition.heartauth.orchestrator.admin.config.security;

import knemognition.heartauth.orchestrator.shared.constants.Authorities;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AdminProperties.class)
public class AdminApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final AdminProperties adminProperties;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String provided = (String) authentication.getCredentials();
        if (provided == null || provided.isBlank()) {
            throw new BadCredentialsException("invalid_api_key");
        }
        if (!provided.equals(adminProperties.getApiKey())) {
            throw new BadCredentialsException("invalid_api_key");
        }


        var authorities = List.of(new SimpleGrantedAuthority(Authorities.ADMIN));

        var authed = new AdminApiKeyAuthenticationToken(Authorities.ADMIN, authorities);

        authed.setDetails(authentication.getDetails());
        return authed;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AdminApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
