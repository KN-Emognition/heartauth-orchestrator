package knemognition.heartauth.orchestrator.admin.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import static knemognition.heartauth.orchestrator.shared.config.mdc.HeaderNames.HEADER_API_KEY;

public class AdminApiKeyAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {
        String key = request.getHeader(HEADER_API_KEY);
        if (key == null || key.isBlank()) return null;
        return new AdminApiKeyAuthenticationToken(key);
    }
}
