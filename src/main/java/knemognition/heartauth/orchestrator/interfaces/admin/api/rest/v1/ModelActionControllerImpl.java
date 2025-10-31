package knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1;

import knemognition.heartauth.orchestrator.interfaces.admin.api.rest.v1.model.ModelActionDto;
import knemognition.heartauth.orchestrator.interfaces.admin.mapper.AdminMapper;
import knemognition.heartauth.orchestrator.modelaction.api.ModelActionApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@PreAuthorize("hasAuthority(T(knemognition.heartauth.orchestrator.shared.constants.Authorities).ADMIN)")
public class ModelActionControllerImpl implements ModelActionController {

    private final ModelActionApi modelActionApi;
    private final AdminMapper adminMapper;

    @Override
    public ResponseEntity<List<ModelActionDto>> getModelAction() {
        log.info("Received request to get model actions");
        modelActionApi.read();
        return ResponseEntity.ok(adminMapper.toDtoList(modelActionApi.read()));
    }
}
