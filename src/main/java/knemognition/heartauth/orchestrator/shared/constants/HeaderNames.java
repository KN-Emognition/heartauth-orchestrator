package knemognition.heartauth.orchestrator.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderNames {
    public static final String MDC_ROUTE_ID = "routeId";
    public static final String HEADER_ROUTE_ID = "X-Route-Id";
    public static final String HEADER_API_KEY = "X-API-Key";
    public static final String ATTR_TENANT_ID = "X-Tenant-Id";
}
