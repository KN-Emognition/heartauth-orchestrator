package knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1;

import knemognition.heartauth.orchestrator.interfaces.admin.AdminService;
import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.model.CreateTenantResponseDto;
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

    private final AdminService adminService;

    @Override
    public ResponseEntity<CreateTenantResponseDto> registerTenant() {
        log.info("[ADMIN-PROFILE] Received request to register tenant");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.register());
    }
}
