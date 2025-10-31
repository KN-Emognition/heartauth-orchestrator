package knemognition.heartauth.orchestrator.admin.app.impl;

import knemognition.heartauth.orchestrator.admin.app.ports.in.TenantService;
import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.CreateTenantResponseDto;
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
public class TenantServiceImpl implements TenantService {

    private final TenantsModule tenantsModule;

    /**
     *  {@inheritDoc}
     */
    @Override
    public CreateTenantResponseDto register() {
        var tenant = tenantsModule.create();
        return CreateTenantResponseDto.builder()
                .id(tenant.getTenantId())
                .apiKey(tenant.getApiKey())
                .build();
    }
}
