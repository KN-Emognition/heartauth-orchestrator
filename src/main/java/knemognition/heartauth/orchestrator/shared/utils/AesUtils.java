package knemognition.heartauth.orchestrator.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AesUtils {
    public static byte[] decrypt(byte[] encryptedData, byte[] salt, ECPublicKey publicKey, ECPrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(privateKey);
        ka.doPhase(publicKey, true);
        byte[] sharedSecret = ka.generateSecret();
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
        hkdf.init(new HKDFParameters(sharedSecret, salt, null));
        byte[] okm = new byte[44];
        hkdf.generateBytes(okm, 0, okm.length);
        SecretKey aesKey = new SecretKeySpec(Arrays.copyOfRange(okm, 0, 32), "AES");
        byte[] iv = Arrays.copyOfRange(okm, 32, 44);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec);
        return cipher.doFinal(encryptedData);
    }
}
