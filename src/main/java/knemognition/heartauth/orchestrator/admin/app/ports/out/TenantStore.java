package knemognition.heartauth.orchestrator.admin.app.ports.out;

import knemognition.heartauth.orchestrator.shared.app.domain.Tenant;
import knemognition.heartauth.orchestrator.shared.app.domain.TenantApiKey;

public interface TenantStore {

    void createTenantWithApiKey(Tenant tenant, TenantApiKey tenantApiKey);
}