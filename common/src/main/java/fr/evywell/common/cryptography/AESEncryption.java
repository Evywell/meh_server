package fr.evywell.common.cryptography;

import fr.evywell.common.logger.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AESEncryption implements EncryptionInterface {

    @Override
    public String encryptFromSecret(String source, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            return Base64.getEncoder().encodeToString(cipher.doFinal(source.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            Log.error("Erreur lors du chiffrement de la source: " + e.toString());
        }
        return null;
    }

    @Override
    public String decryptWithSecret(String source, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            return new String(cipher.doFinal(Base64.getDecoder().decode(source)));
        } catch (Exception e) {
            Log.error("Erreur lors du déchiffrement de la source: " + e.toString());
        }
        return null;
    }

    public static SecretKeySpec getKey(String theKey) {
        byte[] key = theKey.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
