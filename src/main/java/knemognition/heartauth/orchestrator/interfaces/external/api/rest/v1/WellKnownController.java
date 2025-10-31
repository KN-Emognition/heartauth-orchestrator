package knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1;

import com.nimbusds.jose.jwk.ECKey;
import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.api.WellKnownApi;
import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.model.ECJwkDto;
import knemognition.heartauth.orchestrator.interfaces.external.api.rest.v1.model.JwkSetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class WellKnownController implements WellKnownApi {

    private final ECKey publicJwk;

    @Override
    public ResponseEntity<JwkSetDto> getWellKnown() {
        ECJwkDto ec = new ECJwkDto();
        ec.setKty(ECJwkDto.KtyEnum.EC);
        ec.setCrv(ECJwkDto.CrvEnum.P_256);
        ec.setX(publicJwk.getX()
                .toString());
        ec.setY(publicJwk.getY()
                .toString());
        ec.setKid(publicJwk.getKeyID());
        ec.setUse(ECJwkDto.UseEnum.SIG);
        ec.setAlg("ES256");

        ec.setX5u(null);
        ec.setX5c(null);
        ec.setX5t(null);
        ec.setX5tHashS256(null);

        JwkSetDto body = new JwkSetDto();
        body.setKeys(List.of(ec));

        return ResponseEntity.ok(body);
    }
}