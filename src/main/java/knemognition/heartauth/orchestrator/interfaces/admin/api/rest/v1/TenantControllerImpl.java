package knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1;

import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.interfaces.admin.mapper.AdminMapper;
import knemognition.heartauth.orchestrator.tenant.api.TenantApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority(T(knemognition.heartauth.orchestrator.shared.constants.Authorities).ADMIN)")
public class TenantControllerImpl implements TenantsController {

    private final TenantApi tenantApi;
    private final AdminMapper adminMapper;

    @Override
    public ResponseEntity<CreateTenantResponseDto> registerTenant() {
        log.info("Received request to register tenant");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminMapper.toDto(tenantApi.create()));
    }
}
