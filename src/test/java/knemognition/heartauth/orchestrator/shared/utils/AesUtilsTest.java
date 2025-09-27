//package knemognition.heartauth.orchestrator.shared.utils;
//
//import org.bouncycastle.crypto.digests.SHA256Digest;
//import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
//import org.bouncycastle.crypto.params.HKDFParameters;
//import org.junit.jupiter.api.Test;
//
//import javax.crypto.AEADBadTagException;
//import javax.crypto.Cipher;
//import javax.crypto.KeyAgreement;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.GCMParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.interfaces.ECPrivateKey;
//import java.security.interfaces.ECPublicKey;
//import java.security.spec.ECGenParameterSpec;
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class AesUtilsTest {
//
//    private static KeyPair generateEcKeyPair() throws Exception {
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
//        // prime256v1 == secp256r1; widely supported for ECDH
//        kpg.initialize(new ECGenParameterSpec("secp256r1"));
//        return kpg.generateKeyPair();
//    }
//
//    /**
//     * Mirrors the production derivation: ECDH -> HKDF(SHA-256) -> AES-256 key (first 32 bytes) + 12-byte IV (next 12 bytes),
//     * then AES/GCM/NoPadding encrypt.
//     */
//    private static byte[] encrypt(byte[] plaintext, byte[] salt, ECPublicKey recipientPublic, ECPrivateKey senderPrivate) throws Exception {
//        // ECDH shared secret
//        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
//        ka.init(senderPrivate);
//        ka.doPhase(recipientPublic, true);
//        byte[] sharedSecret = ka.generateSecret();
//
//        // HKDF to 44-byte okm
//        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA256Digest());
//        hkdf.init(new HKDFParameters(sharedSecret, salt, null));
//        byte[] okm = new byte[44];
//        hkdf.generateBytes(okm, 0, okm.length);
//
//        SecretKey aesKey = new SecretKeySpec(Arrays.copyOfRange(okm, 0, 32), "AES");
//        byte[] iv = Arrays.copyOfRange(okm, 32, 44);
//
//        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//        GCMParameterSpec gcm = new GCMParameterSpec(128, iv);
//        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcm);
//        return cipher.doFinal(plaintext);
//    }
//
//    @Test
//    void decrypt_roundTrip_success() throws Exception {
//        // Generate two EC keypairs to simulate two parties (Alice <-> Bob)
//        KeyPair alice = generateEcKeyPair();
//        KeyPair bob = generateEcKeyPair();
//
//        byte[] salt = "fixed-test-salt-16".getBytes(); // 16 bytes (can be any length)
//        byte[] plaintext = "hello, aes-gcm with ecdh+hkdf".getBytes();
//
//        // Encrypt as Alice -> Bob
//        byte[] ciphertext = encrypt(plaintext, salt, (ECPublicKey) bob.getPublic(), (ECPrivateKey) alice.getPrivate());
//
//        // Decrypt as Bob <- Alice (note the swapped keys compared to encryption)
//        byte[] decrypted = AesUtils.decrypt(
//                ciphertext,
//                salt,
//                (ECPublicKey) alice.getPublic(),
//                (ECPrivateKey) bob.getPrivate()
//        );
//
//        assertArrayEquals(plaintext, decrypted, "Decrypted plaintext should match original");
//    }
//
//    @Test
//    void decrypt_withWrongSalt_fails() throws Exception {
//        KeyPair alice = generateEcKeyPair();
//        KeyPair bob = generateEcKeyPair();
//
//        byte[] salt = "original-salt-123".getBytes();
//        byte[] badSalt = "original-salt-12X".getBytes(); // same length, 1 byte different
//        byte[] plaintext = "msg".getBytes();
//
//        byte[] ciphertext = encrypt(plaintext, salt, (ECPublicKey) bob.getPublic(), (ECPrivateKey) alice.getPrivate());
//
//        assertThrows(AEADBadTagException.class, () ->
//                        AesUtils.decrypt(
//                                ciphertext,
//                                badSalt,
//                                (ECPublicKey) alice.getPublic(),
//                                (ECPrivateKey) bob.getPrivate()
//                        ),
//                "GCM auth should fail when salt differs (HKDF derives different key/IV)"
//        );
//    }
//
//    @Test
//    void decrypt_withWrongPrivateKey_fails() throws Exception {
//        KeyPair alice = generateEcKeyPair();
//        KeyPair bob = generateEcKeyPair();
//        KeyPair mallory = generateEcKeyPair(); // wrong private key
//
//        byte[] salt = "another-salt-456".getBytes();
//        byte[] plaintext = "secret".getBytes();
//
//        byte[] ciphertext = encrypt(plaintext, salt, (ECPublicKey) bob.getPublic(), (ECPrivateKey) alice.getPrivate());
//
//        assertThrows(AEADBadTagException.class, () ->
//                        AesUtils.decrypt(
//                                ciphertext,
//                                salt,
//                                (ECPublicKey) alice.getPublic(),
//                                (ECPrivateKey) mallory.getPrivate() // wrong party
//                        ),
//                "Decryption must fail with a mismatched private key"
//        );
//    }
//
//    @Test
//    void decrypt_withTamperedCiphertext_fails() throws Exception {
//        KeyPair alice = generateEcKeyPair();
//        KeyPair bob = generateEcKeyPair();
//
//        byte[] salt = "tamper-test-salt".getBytes();
//        byte[] plaintext = "authenticated data".getBytes();
//
//        byte[] ciphertext = encrypt(plaintext, salt, (ECPublicKey) bob.getPublic(), (ECPrivateKey) alice.getPrivate());
//
//        // Flip a byte somewhere (avoid IndexOutOfBounds)
//        byte[] tampered = Arrays.copyOf(ciphertext, ciphertext.length);
//        tampered[tampered.length / 2] ^= 0x01;
//
//        assertThrows(AEADBadTagException.class, () ->
//                        AesUtils.decrypt(
//                                tampered,
//                                salt,
//                                (ECPublicKey) alice.getPublic(),
//                                (ECPrivateKey) bob.getPrivate()
//                        ),
//                "GCM must detect tampering and fail authentication"
//        );
//    }
//}
