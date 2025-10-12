package knemognition.heartauth.orchestrator.admin.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.admin.app.ports.in.TenantService;
import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.CreateTenantResponseDto;
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
public class TenantController implements TenantsApi {

    private final TenantService tenantService;

    @Override
    public ResponseEntity<CreateTenantResponseDto> registerTenant() {
        log.info("Received request to register tenant");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tenantService.register());
    }
}
