package knemognition.heartauth.orchestrator.external.config.rest.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knemognition.heartauth.orchestrator.external.app.domain.QrClaims;
import knemognition.heartauth.orchestrator.external.app.ports.in.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PairingJwtInterceptor implements HandlerInterceptor {
    public static final String REQ_ATTR_QR_CLAIMS = "qrClaims";
    private final JwtService jwtService;
    private static final Set<String> TARGET_METHODS =
            Set.of("externalPairingConfirm", "externalPairingInit");

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if (handler instanceof HandlerMethod hm && TARGET_METHODS.contains(hm.getMethod().getName())) {
            QrClaims claims = jwtService.process();
            req.setAttribute(REQ_ATTR_QR_CLAIMS, claims);
        }
        return true;
    }
}