package knemognition.heartauth.orchestrator.interfaces.admin.config.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AdminApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String rawKey;
    private final String principal;

    public AdminApiKeyAuthenticationToken(String rawKey) {
        super(null);
        this.rawKey = rawKey;
        this.principal = null;
        setAuthenticated(false);
    }

    public AdminApiKeyAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.rawKey = null;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return rawKey;
    }

    @Override
    public Object getPrincipal() {
        return principal != null ? principal : "anonymous";
    }
}
