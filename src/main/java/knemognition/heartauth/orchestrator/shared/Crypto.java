package knemognition.heartauth.orchestrator.shared;


import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class Crypto {
    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Crypto() {
    }

    public static ECPrivateKey loadEcPrivateKey(java.io.InputStream in, char[] password) throws Exception {
        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             PEMParser parser = new PEMParser(reader)) {

            Object obj = parser.readObject();
            if (obj == null) throw new IllegalArgumentException("Empty private key PEM");

            JcaPEMKeyConverter conv = new JcaPEMKeyConverter().setProvider("BC");

            if (obj instanceof PKCS8EncryptedPrivateKeyInfo enc) {
                if (password == null || password.length == 0) {
                    throw new IllegalArgumentException("Private key is encrypted but no password was provided");
                }
                var decryptor = new JceOpenSSLPKCS8DecryptorProviderBuilder()
                        .setProvider("BC").build(password);
                PrivateKeyInfo pki = enc.decryptPrivateKeyInfo(decryptor);
                PrivateKey pk = conv.getPrivateKey(pki);
                return (ECPrivateKey) pk;
            }

            if (obj instanceof PrivateKeyInfo pki) {
                PrivateKey pk = conv.getPrivateKey(pki);
                return (ECPrivateKey) pk;
            }

            if (obj instanceof PEMKeyPair kp) {
                KeyPair keyPair = conv.getKeyPair(kp);
                return (ECPrivateKey) keyPair.getPrivate();
            }

            throw new IllegalArgumentException("Unsupported private key PEM type: " + obj.getClass().getName());
        }
    }

    public static ECPublicKey loadEcPublicKey(java.io.InputStream in) throws Exception {
        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             PEMParser parser = new PEMParser(reader)) {

            Object obj = parser.readObject();
            if (obj == null) throw new IllegalArgumentException("Empty public key PEM");

            JcaPEMKeyConverter conv = new JcaPEMKeyConverter().setProvider("BC");

            if (obj instanceof SubjectPublicKeyInfo spki) {
                PublicKey pub = conv.getPublicKey(spki);
                return (ECPublicKey) pub;
            }
            if (obj instanceof org.bouncycastle.util.io.pem.PemObject po &&
                    "PUBLIC KEY".equalsIgnoreCase(po.getType())) {
                var kf = KeyFactory.getInstance("EC");
                var spec = new X509EncodedKeySpec(po.getContent());
                return (ECPublicKey) kf.generatePublic(spec);
            }

            throw new IllegalArgumentException("Unsupported public key PEM type: " + obj.getClass().getName());
        }
    }
}
