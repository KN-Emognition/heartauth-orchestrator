package knemognition.heartauth.orchestrator.shared.mdc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderNames {
    public static final String MDC_ROUTE_ID="routeId";
    public static final String HEADER_ROUTE_ID = "X-Route-Id";
}
