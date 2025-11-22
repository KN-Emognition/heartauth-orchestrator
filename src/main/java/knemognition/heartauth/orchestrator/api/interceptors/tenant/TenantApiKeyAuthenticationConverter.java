package knemognition.heartauth.orchestrator.api.interceptors.tenant;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;


public class TenantApiKeyAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String key = request.getHeader(HeaderNames.HEADER_API_KEY);
        if (key == null || key.isBlank()) return null;
        return new TenantApiKeyAuthenticationToken(key);
    }
}

