package knemognition.heartauth.orchestrator.api.rest.v1;

import knemognition.heartauth.orchestrator.api.DtoMapper;
import knemognition.heartauth.orchestrator.api.rest.v1.admin.api.ModelActionApi;
import knemognition.heartauth.orchestrator.api.rest.v1.admin.api.TenantsApi;
import knemognition.heartauth.orchestrator.api.rest.v1.admin.model.CreateTenantResponseDto;
import knemognition.heartauth.orchestrator.modelapi.api.ModelApiModule;
import knemognition.heartauth.orchestrator.shared.constants.SpringProfiles;
import knemognition.heartauth.orchestrator.tenants.api.TenantsModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority(T(knemognition.heartauth.orchestrator.shared.constants.Authorities).ADMIN)")
@Profile(SpringProfiles.ADMIN)
public class AdminController implements TenantsApi, ModelActionApi {

    private final TenantsModule tenantsModule;
    private final ModelApiModule modelApiModule;
    private final DtoMapper mapper;

    @Override
    public ResponseEntity<CreateTenantResponseDto> registerTenant() {
        log.info("[ADMIN-CONTROLLER] Received request to register tenant");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toDto(tenantsModule.create()));
    }

    @Override
    public ResponseEntity<Map<String, Object>> getModelAction() {
        log.info("[ADMIN-CONTROLLER] Received request to get combined model API");
        return ResponseEntity.ok(modelApiModule.getCombinedModelApi());
    }
}
