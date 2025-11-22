package knemognition.heartauth.orchestrator.api.interceptors.admin;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

class AdminApiKeyAuthenticationConverterTest {

    @Test
    void shouldConvertWhenHeaderPresent() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(HeaderNames.HEADER_API_KEY)).thenReturn("key");
        var converter = new AdminApiKeyAuthenticationConverter();

        Authentication authentication = converter.convert(request);

        assertThat(authentication).isInstanceOf(AdminApiKeyAuthenticationToken.class);
        assertThat(authentication.getCredentials()).isEqualTo("key");
    }

    @Test
    void shouldReturnNullWhenHeaderMissing() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(HeaderNames.HEADER_API_KEY)).thenReturn(null);

        assertThat(new AdminApiKeyAuthenticationConverter().convert(request)).isNull();
    }
}
