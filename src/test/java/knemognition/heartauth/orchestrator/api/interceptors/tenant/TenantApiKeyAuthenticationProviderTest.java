package knemognition.heartauth.orchestrator.api.interceptors.tenant;

import knemognition.heartauth.orchestrator.shared.constants.Authorities;
import knemognition.heartauth.orchestrator.tenants.api.TenantRead;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class TenantApiKeyAuthenticationProviderTest {

    private final TenantsModule tenantsModule = Mockito.mock(TenantsModule.class);
    private final TenantApiKeyAuthenticationProvider provider = new TenantApiKeyAuthenticationProvider(tenantsModule);

    @Test
    void shouldAuthenticateAndAttachTenantId() {
        UUID tenantId = UUID.randomUUID();
        when(tenantsModule.getByApiKey("key")).thenReturn(Optional.of(TenantRead.builder()
                .tenantId(tenantId)
                .build()));

        Authentication result = provider.authenticate(new TenantApiKeyAuthenticationToken("key"));

        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getPrincipal()).isEqualTo(tenantId);
        assertThat(result.getAuthorities()).singleElement()
                .extracting("authority")
                .isEqualTo(Authorities.TENANT);
    }

    @Test
    void shouldRejectMissingKey() {
        assertThatThrownBy(() -> provider.authenticate(new TenantApiKeyAuthenticationToken("")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void shouldRejectUnknownKey() {
        when(tenantsModule.getByApiKey("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> provider.authenticate(new TenantApiKeyAuthenticationToken("unknown")))
                .isInstanceOf(BadCredentialsException.class);
    }
}
