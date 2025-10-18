package knemognition.heartauth.orchestrator.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderNames {
    public static final String MDC_CORRELATION_ID = "correlationId";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-Id";
    public static final String HEADER_API_KEY = "X-API-Key";
    public static final String ATTR_TENANT_ID = "X-Tenant-Id";
}
