package com.ifast.ipaymy.backend.util.cryptographic;

import com.ifast.ipaymy.backend.util.codec.Base64Utils;
import com.ifast.ipaymy.backend.util.cryptographic.constants.SecurityConstants;
import com.ifast.ipaymy.backend.util.cryptographic.exception.CryptographicException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.HexFormat;

import static com.ifast.ipaymy.backend.util.cryptographic.CryptoUtils.DefaultConstants.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class CryptoUtils {

    public static KeyPair generateKeyPair(String keyPairGenerator,
                                          AlgorithmParameterSpec algorithmParameterSpec) {
        try {
            return ShadedCryptoUtils.generateKeyPair(keyPairGenerator, algorithmParameterSpec);
        } catch (GeneralSecurityException e) {
            throw new CryptographicException(e.getMessage(), e);
        }
    }

    public static SecretKey generateAES256SecretKey() {
        try {
            return ShadedCryptoUtils.generateRandomSecretKey(DEFAULT_SECRET_KEY_GENERATOR_ALGORITHM,
                DEFAULT_SECRET_KEY_SIZE);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static String sha256HashHax(String message) {
        try {
            return ShadedCryptoUtils.hashHex(message, DEFAULT_DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographicException(e.getMessage(), e);
        }
    }

    public static String encrypt(String plainText, String passphrase) {
        return encrypt(plainText.getBytes(StandardCharsets.UTF_8), passphrase.toCharArray());
    }

    public static String encrypt(byte[] plainTextBytes, char[] password) {
        byte[] salt = salt();
        SecretKey secretKey = derivePBESecretKey(password, salt);
        return encrypt(plainTextBytes, secretKey, salt);
    }

    public static String encrypt(byte[] plainTextBytes, SecretKey secretKey, byte[] salt) {
        try {
            Cipher cipher = initEncryptCipher(secretKey);

            byte[] iv = cipher.getIV();
            byte[] aad = ByteBuffer.allocate(DEFAULT_SALT_LENGTH + DEFAULT_IV_LENGTH).put(salt)
                .put(iv).array();
            cipher.updateAAD(aad);

            byte[] cipherTextBytes = cipher.doFinal(plainTextBytes);
            byte[] outputBytes = ByteBuffer
                .allocate(DEFAULT_SALT_LENGTH + DEFAULT_IV_LENGTH + cipherTextBytes.length)
                .put(salt).put(iv).put(cipherTextBytes).array();
            return Base64Utils.encodeToString(outputBytes);
        } catch (GeneralSecurityException e) {
            throw new CryptographicException(e.getMessage(), e);
        }
    }

    public static String decrypt(String b64CipherText, String passphrase) {
        return new String(decryptToBytes(b64CipherText, passphrase.toCharArray()), DEFAULT_CHARSET);
    }

    public static byte[] decryptToBytes(String b64CipherText, char[] password) {
        try {
            byte[] blob = Base64Utils.decodeToBytes(b64CipherText);
            ByteBuffer byteBuffer = ByteBuffer.wrap(blob);

            byte[] salt = new byte[DEFAULT_SALT_LENGTH];
            byteBuffer.get(salt);

            byte[] iv = new byte[DEFAULT_IV_LENGTH];
            byteBuffer.get(iv);

            byte[] cipherTextBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherTextBytes);

            SecretKey secretKey = CryptoUtils.derivePBESecretKey(password, salt);
            Cipher cipher = initDecryptCipher(secretKey, iv);
            byte[] aad = ByteBuffer.allocate(DEFAULT_SALT_LENGTH + DEFAULT_IV_LENGTH).put(salt)
                .put(iv).array();
            cipher.updateAAD(aad);

            return cipher.doFinal(cipherTextBytes);
        } catch (Exception e) {
            throw new CryptographicException(e.getMessage(), e);
        }
    }

    public static SecretKey derivePBESecretKey(String passphrase, byte[] salt) {
        return derivePBESecretKey(passphrase.toCharArray(), salt);
    }

    public static SecretKey derivePBESecretKey(char[] password, byte[] salt) {
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, DEFAULT_ITERATIONS,
                DEFAULT_SECRET_KEY_SIZE);
            SecretKey secretKey = ShadedCryptoUtils.deriveSecretKey(pbeKeySpec,
                DEFAULT_SECRET_KEY_FACTORY_ALGORITHM, DEFAULT_CIPHER_ALGORITHM);
            // To avoid leaking password due to memory dump
            pbeKeySpec.clearPassword();
            return secretKey;
        } catch (GeneralSecurityException e) {
            throw new CryptographicException(e.getMessage(), e);
        }
    }

    private static SecretKey deriveHashedSecretKey(String passphrase) {
        try {
            return ShadedCryptoUtils.deriveHashedSecretKey(passphrase, DEFAULT_DIGEST_ALGORITHM,
                DEFAULT_CIPHER_ALGORITHM, DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static Cipher initEncryptCipher(SecretKey secretKey) {
        try {
            return ShadedCryptoUtils.initEncryptCipher(DEFAULT_CIPHER_TRANSFORMATION, secretKey,
                gcmParameterSpec(iv()));
        } catch (Exception e) {
            // The default cipher should always be available
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static Cipher initDecryptCipher(SecretKey secretKey, byte[] iv) {
        try {
            return ShadedCryptoUtils.initDecryptCipher(DEFAULT_CIPHER_TRANSFORMATION, secretKey,
                gcmParameterSpec(iv));
        } catch (Exception e) {
            // The default cipher should always be available
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static GCMParameterSpec gcmParameterSpec(byte[] initializationVector) {
        return new GCMParameterSpec(DEFAULT_GCM_TAG_LENGTH_BITS, initializationVector);
    }

    private static byte[] salt() {
        return ShadedCryptoUtils.salt(DEFAULT_SALT_LENGTH);
    }

    private static byte[] iv() {
        return ShadedCryptoUtils.iv(DEFAULT_IV_LENGTH);
    }

    @RequiredArgsConstructor(access = AccessLevel.NONE)
    private static class ShadedCryptoUtils {

        static SecretKey generateRandomSecretKey(String algorithm, int keySize)
            throws NoSuchAlgorithmException {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(keySize);
            return keyGenerator.generateKey();
        }

        static KeyPair generateKeyPair(String algorithm,
                                       AlgorithmParameterSpec algorithmParameterSpec)
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(algorithmParameterSpec);
            return keyPairGenerator.generateKeyPair();
        }

        static String hashHex(String message, String algorithm) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(message.getBytes(DEFAULT_CHARSET));
            return HexFormat.of().formatHex(hash);
        }

        static SecretKey deriveHashedSecretKey(String passphrase,
                                               String digest,
                                               String cipher,
                                               Charset charset)
            throws GeneralSecurityException {
            byte[] key = passphrase.getBytes(charset);
            MessageDigest sha = MessageDigest.getInstance(digest);
            key = sha.digest(key);
            return new SecretKeySpec(key, cipher);
        }

        static SecretKey deriveSecretKey(KeySpec keySpec, String secretKeyFactory, String cipher)
            throws GeneralSecurityException {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyFactory);
            byte[] keyBytes = factory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, cipher);
        }

        static Cipher initEncryptCipher(String transformation,
                                        SecretKey secretKey,
                                        AlgorithmParameterSpec algorithmParameterSpec)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                   InvalidAlgorithmParameterException {
            return initCipher(transformation, Cipher.ENCRYPT_MODE, secretKey,
                algorithmParameterSpec);
        }

        static Cipher initDecryptCipher(String transformation,
                                        SecretKey secretKey,
                                        AlgorithmParameterSpec algorithmParameterSpec)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                   InvalidAlgorithmParameterException {
            return initCipher(transformation, Cipher.DECRYPT_MODE, secretKey,
                algorithmParameterSpec);
        }

        static Cipher initCipher(String transformation,
                                 int mode,
                                 SecretKey secretKey,
                                 AlgorithmParameterSpec algorithmParameterSpec)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                   InvalidAlgorithmParameterException {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(mode, secretKey, algorithmParameterSpec);
            return cipher;
        }

        static byte[] salt(int length) {
            return randomBytes(length);
        }

        static byte[] iv(int length) {
            return randomBytes(length);
        }

        static byte[] randomBytes(int length) {
            byte[] iv = new byte[length];
            new SecureRandom().nextBytes(iv);
            return iv;
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.NONE)
    static class DefaultConstants {

        // Cipher
        public static final String DEFAULT_CIPHER_ALGORITHM = SecurityConstants.Cipher.Algorithm.AES;
        public static final String DEFAULT_CIPHER_MODE = SecurityConstants.Cipher.Mode.GCM;
        public static final String DEFAULT_CIPHER_PADDING = SecurityConstants.Cipher.Padding.NO_PADDING;
        public static final String DEFAULT_CIPHER_TRANSFORMATION = SecurityConstants.Cipher.transformation(
            DEFAULT_CIPHER_ALGORITHM,
            DEFAULT_CIPHER_MODE,
            DEFAULT_CIPHER_PADDING
        );
        public static final int DEFAULT_SALT_LENGTH = 16;
        public static final int DEFAULT_IV_LENGTH = 12;

        // GCM Spec
        public static final int DEFAULT_GCM_TAG_LENGTH_BITS = 128;

        public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

        // Key Pair Generator
        public static final String DEFAULT_KEY_PAIR_ALGORITHM = SecurityConstants.KeyPairGenerator.RSA;
        public static final int DEFAULT_KEY_PAIR_SIZE = 3072;

        // Key Generator
        public static final int DEFAULT_ITERATIONS = 210_000;
        public static final int DEFAULT_SECRET_KEY_SIZE = 256;
        public static final String DEFAULT_SECRET_KEY_FACTORY_ALGORITHM = SecurityConstants.SecretKeyFactory.PBKDF2_WITH_HMACSHA256;
        public static final String DEFAULT_SECRET_KEY_GENERATOR_ALGORITHM = SecurityConstants.KeyGenerator.AES;

        // Message Digest
        public static final String DEFAULT_DIGEST_ALGORITHM = SecurityConstants.MessageDigest.SHA256;


    }

}
