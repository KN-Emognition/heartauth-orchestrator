package knemognition.heartauth.orchestrator.external.interfaces.rest.v1;

import com.nimbusds.jose.jwk.ECKey;
import knemognition.heartauth.orchestrator.external.api.WellKnownApi;
import knemognition.heartauth.orchestrator.external.model.ECJwk;
import knemognition.heartauth.orchestrator.external.model.JwkSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RequiredArgsConstructor
public class WellKnownController implements WellKnownApi {

    private final ECKey publicJwk;

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