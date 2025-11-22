package knemognition.heartauth.orchestrator.api.interceptors.tenant;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class TenantApiKeyAuthenticationConverterTest {

    @Test
    void shouldConvertHeaderToToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(HeaderNames.HEADER_API_KEY)).thenReturn("api-key");

        var auth = new TenantApiKeyAuthenticationConverter().convert(request);

        assertThat(auth).isInstanceOf(TenantApiKeyAuthenticationToken.class);
        assertThat(auth.getCredentials()).isEqualTo("api-key");
    }

    @Test
    void shouldReturnNullWhenHeaderMissing() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(HeaderNames.HEADER_API_KEY)).thenReturn(null);

        assertThat(new TenantApiKeyAuthenticationConverter().convert(request)).isNull();
    }
}
