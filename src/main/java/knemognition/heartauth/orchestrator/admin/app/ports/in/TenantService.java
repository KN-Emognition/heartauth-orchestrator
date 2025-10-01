package knemognition.heartauth.orchestrator.admin.app.ports.in;

import knemognition.heartauth.orchestrator.admin.model.CreateTenantResponse;

public interface TenantService {
    CreateTenantResponse register();
}
