package knemognition.heartauth.orchestrator.admin.app;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
class HttpAccessLgoFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws java.io.IOException, jakarta.servlet.ServletException {
        try {
            chain.doFilter(req, res);
        } finally {
            log.info("HTTP {} {} -> {}", req.getMethod(), req.getRequestURI(), res.getStatus());
        }
    }
}
