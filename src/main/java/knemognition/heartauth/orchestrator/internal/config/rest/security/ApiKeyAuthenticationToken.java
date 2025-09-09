package knemognition.heartauth.orchestrator.internal.config.rest.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String keyId;
    private final String rawKey;

    public ApiKeyAuthenticationToken(String rawKey) {
        super(null);
        this.rawKey = rawKey;
        this.keyId = null;
        setAuthenticated(false);
    }

    public ApiKeyAuthenticationToken(String keyId, Collection<? extends GrantedAuthority> auths) {
        super(auths);
        this.keyId = keyId;
        this.rawKey = null;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return rawKey;
    }

    @Override
    public Object getPrincipal() {
        return keyId;
    }
}
