package knemognition.heartauth.orchestrator.internal.config.rest.security;

import knemognition.heartauth.orchestrator.internal.config.firebase.FirebaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(ApiKeyProperties.class)
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {
    private final ApiKeyProperties props;

    public ApiKeyAuthenticationProvider(ApiKeyProperties props) {
        this.props = props;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String provided = (String) authentication.getCredentials(); // raw key
        var match = props.keys().stream().filter(k -> k.matches(provided)).findFirst().orElse(null);
        if (match == null)
            throw new BadCredentialsException("invalid_api_key");

        var authorities = match.roles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        var authed = new ApiKeyAuthenticationToken(match.id(), authorities);
        authed.setDetails(authentication.getDetails());
        return authed;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}