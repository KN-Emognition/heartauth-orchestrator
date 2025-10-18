package knemognition.heartauth.orchestrator.shared.config.rest;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knemognition.heartauth.orchestrator.shared.constants.HeaderNames;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String routeId = request.getHeader(HeaderNames.HEADER_CORRELATION_ID);
        if (routeId == null || routeId.isBlank()) {
            routeId = UUID.randomUUID()
                    .toString();
        }
        MDC.put(HeaderNames.MDC_CORRELATION_ID, routeId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(HeaderNames.MDC_CORRELATION_ID);
        }
    }
}
