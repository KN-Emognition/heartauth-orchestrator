package knemognition.heartauth.orchestrator.api.interceptors.tenant;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class TenantApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String rawKey;
    private final UUID tenantId;

    public TenantApiKeyAuthenticationToken(String rawKey) {
        super(null);
        this.rawKey = rawKey;
        this.tenantId = null;
        setAuthenticated(false);
    }

    public TenantApiKeyAuthenticationToken(UUID tenantId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.rawKey = null;
        this.tenantId = tenantId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return rawKey;
    }

    @Override
    public Object getPrincipal() {
        return tenantId != null ? tenantId : "anonymous";
    }
}