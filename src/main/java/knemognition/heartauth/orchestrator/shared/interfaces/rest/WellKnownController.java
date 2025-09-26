package knemognition.heartauth.orchestrator.shared.interfaces.rest;

import knemognition.heartauth.orchestrator.wellknown.api.WellKnownApi;
import knemognition.heartauth.orchestrator.wellknown.model.ECJwk;
import knemognition.heartauth.orchestrator.wellknown.model.JwkSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class WellKnownController implements WellKnownApi {

    private final com.nimbusds.jose.jwk.ECKey publicJwk;

    @Override
    public ResponseEntity<JwkSet> getJwks() {
        ECJwk ec = new ECJwk();
        ec.setKty(ECJwk.KtyEnum.EC);
        ec.setCrv(ECJwk.CrvEnum.P_256);
        ec.setX(publicJwk.getX().toString());
        ec.setY(publicJwk.getY().toString());
        ec.setKid(publicJwk.getKeyID());
        ec.setUse(ECJwk.UseEnum.SIG);
        ec.setAlg("ES256");

        ec.setX5u(null);
        ec.setX5c(null);
        ec.setX5t(null);
        ec.setX5tHashS256(null);

        JwkSet body = new JwkSet();
        body.setKeys(List.of(ec));

        return ResponseEntity.ok(body);
    }
}