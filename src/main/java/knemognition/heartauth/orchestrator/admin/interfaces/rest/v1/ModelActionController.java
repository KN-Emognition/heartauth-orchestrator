package knemognition.heartauth.orchestrator.admin.interfaces.rest.v1;

import knemognition.heartauth.orchestrator.admin.app.ports.in.ModelActionService;
import knemognition.heartauth.orchestrator.admin.interfaces.rest.v1.model.ModelActionDto;
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
public class ModelActionController implements ModelActionApi {

    private final ModelActionService modelActionService;

    @Override
    public ResponseEntity<List<ModelActionDto>> getModelAction() {
        log.info("Received request to get model actions");
        return ResponseEntity.ok(modelActionService.read());
    }
}
