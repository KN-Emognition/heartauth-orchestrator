package knemognition.heartauth.orchestrator.interfaces.admin;

import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *  {@inheritDoc}
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final TenantsModule tenantsModule;

    public CreateTenantResponseDto register() {
        var tenant = tenantsModule.create();
        return CreateTenantResponseDto.builder()
                .id(tenant.getTenantId())
                .apiKey(tenant.getApiKey())
                .build();
    }
}
