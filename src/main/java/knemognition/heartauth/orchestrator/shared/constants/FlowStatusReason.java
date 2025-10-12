package knemognition.heartauth.orchestrator.shared.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlowStatusReason {
    public static final String FLOW_CREATED = "New flow created.";
    public static final String FLOW_RECREATED = "Flow recreated.";
    public static final String FLOW_COMPLETED_SUCCESSFULLY_WITH_AUTHENTICATION = "Flow completed successfully with authentication.";
    public static final String FLOW_COMPLETED_SUCCESSFULLY = "Flow completed successfully.";
    public static final String FLOW_DENIED_WITHOUT_AUTHENTICATION = "Flow denied without authentication.";
    public static final String FLOW_DENIED_WITH_AUTHENTICATION_FAILURE = "Flow denied with authentication failure.";
    public static final String FLOW_DENIED_BY_RECREATING_FLOW = "Flow denied by recreating flow.";
    public static final String FLOW_INITIALIZED_ON_MOBILE_DEVICE = "Flow initialized on mobile device.";
}
