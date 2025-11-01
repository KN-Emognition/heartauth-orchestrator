package knemognition.heartauth.orchestrator.security.app.impl.internal;

import jakarta.servlet.http.HttpServletRequest;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;


public class InternalApiKeyAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        String key = request.getHeader(HeaderNames.HEADER_API_KEY);
        if (key == null || key.isBlank()) return null;
        return new InternalApiKeyAuthenticationToken(key);
    }
}

