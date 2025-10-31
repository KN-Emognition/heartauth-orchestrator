package knemognition.heartauth.orchestrator.security.app.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeyLoader {
    public static ECPrivateKey loadEcPrivateKey(InputStream in) throws Exception {
        byte[] der = pemToDer(in, "PRIVATE KEY"); // PKCS#8
        var spec = new PKCS8EncodedKeySpec(der);
        return (ECPrivateKey) java.security.KeyFactory.getInstance("EC")
                .generatePrivate(spec);
    }

    public static ECPublicKey loadEcPublicKey(InputStream in) throws Exception {
        byte[] der = pemToDer(in, "PUBLIC KEY");
        var spec = new X509EncodedKeySpec(der);
        return (ECPublicKey) java.security.KeyFactory.getInstance("EC")
                .generatePublic(spec);
    }

    private static byte[] pemToDer(InputStream in, String type) throws Exception {
        String pem = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.US_ASCII);
        String start = "-----BEGIN " + type + "-----";
        String end = "-----END " + type + "-----";
        String base64 = pem.substring(pem.indexOf(start) + start.length(), pem.indexOf(end))
                .replaceAll("\\s", "");
        return java.util.Base64.getDecoder()
                .decode(base64);
    }

    public static String toPem(Key key, String type) {
        String base64 = Base64.getMimeEncoder(64, new byte[]{'\n'})
                .encodeToString(key.getEncoded());
        return "-----BEGIN " + type + "-----\n" +
                base64 + "\n" +
                "-----END " + type + "-----";
    }

    public static KeyPair createEphemeralKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
            kpg.initialize(new ECGenParameterSpec("secp256r1"));
            return kpg.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
