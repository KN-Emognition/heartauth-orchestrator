package knemognition.heartauth.orchestrator.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeyLoader {
    public static ECPrivateKey loadEcPrivateKey(InputStream in) throws Exception {
        byte[] der = pemToDer(in, "PRIVATE KEY"); // PKCS#8
        var spec = new java.security.spec.PKCS8EncodedKeySpec(der);
        return (ECPrivateKey) java.security.KeyFactory.getInstance("EC").generatePrivate(spec);
    }
    public static ECPublicKey loadEcPublicKey(InputStream in) throws Exception {
        byte[] der = pemToDer(in, "PUBLIC KEY");
        var spec = new java.security.spec.X509EncodedKeySpec(der);
        return (ECPublicKey) java.security.KeyFactory.getInstance("EC").generatePublic(spec);
    }
    private static byte[] pemToDer(InputStream in, String type) throws Exception {
        String pem = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.US_ASCII);
        String start = "-----BEGIN " + type + "-----";
        String end = "-----END " + type + "-----";
        String base64 = pem.substring(pem.indexOf(start) + start.length(), pem.indexOf(end))
                .replaceAll("\\s", "");
        return java.util.Base64.getDecoder().decode(base64);
    }
}
