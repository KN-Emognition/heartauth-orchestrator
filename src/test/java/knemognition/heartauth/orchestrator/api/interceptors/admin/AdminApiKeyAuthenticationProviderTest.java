package knemognition.heartauth.orchestrator.api.interceptors.admin;

import knemognition.heartauth.orchestrator.shared.constants.Authorities;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminApiKeyAuthenticationProviderTest {

    @Test
    void shouldAuthenticateWhenKeyMatches() {
        AdminProperties props = new AdminProperties();
        props.setKey("secret");
        var provider = new AdminApiKeyAuthenticationProvider(props);
        Authentication request = new AdminApiKeyAuthenticationToken("secret");

        Authentication result = provider.authenticate(request);

        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getAuthorities()).singleElement()
                .extracting("authority")
                .isEqualTo(Authorities.ADMIN);
    }

    @Test
    void shouldRejectWhenKeyMissing() {
        AdminProperties props = new AdminProperties();
        props.setKey("secret");
        var provider = new AdminApiKeyAuthenticationProvider(props);
        Authentication request = new AdminApiKeyAuthenticationToken("");

        assertThatThrownBy(() -> provider.authenticate(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void shouldRejectWhenKeyMismatch() {
        AdminProperties props = new AdminProperties();
        props.setKey("secret");
        var provider = new AdminApiKeyAuthenticationProvider(props);
        Authentication request = new AdminApiKeyAuthenticationToken("wrong");

        assertThatThrownBy(() -> provider.authenticate(request))
                .isInstanceOf(BadCredentialsException.class);
    }
}
