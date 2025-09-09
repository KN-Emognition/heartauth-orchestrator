package knemognition.heartauth.orchestrator.external.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.interfaces.ECPublicKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateNonce {
    private String nonce;
    private String signature;
    private ECPublicKey pub;
}
