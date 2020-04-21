package fr.evywell.common.cryptography;

public interface EncryptionInterface {

    String encryptFromSecret(String source, String key);
    String decryptWithSecret(String source, String key);

}
