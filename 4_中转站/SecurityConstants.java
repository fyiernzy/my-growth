package com.ifast.ipaymy.backend.util.cryptographic.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The standard names of security algorithms.
 * See Java Security Standard Algorithm Names Specification for details.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public final class SecurityConstants {

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class MessageDigest {

        public static final String MD2 = "MD2";
        public static final String MD5 = "MD5";
        public static final String SHA1 = "SHA-1";
        public static final String SHA224 = "SHA-224";
        public static final String SHA256 = "SHA-256";
        public static final String SHA384 = "SHA-384";
        public static final String SHA512 = "SHA-512";
        public static final String SHA512_224 = "SHA-512/224";
        public static final String SHA512_256 = "SHA-512/256";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class Cipher {

        public static String transformation(String algorithm, String mode, String padding) {
            return String.format("%s/%s/%s", algorithm, mode, padding);
        }

        @NoArgsConstructor(access = AccessLevel.NONE)
        public static class Algorithm {

            public static final String AES = "AES";
            public static final String AES_WRAP = "AESWrap";
            public static final String AES_WRAP_PAD = "AESWrapPad";
            public static final String ARCFOUR = "ARCFOUR";
            public static final String BLOWFISH = "Blowfish";
            public static final String CHACHA20 = "ChaCha20";
            public static final String CHACHA20_POLY1305 = "ChaCha20-Poly1305";
            public static final String DES = "DES";
            public static final String DES_EDE = "DESede";
            public static final String DES_EDE_WRAP = "DESedeWrap";
            public static final String ECIES = "ECIES";
            public static final String RC2 = "RC2";
            public static final String RC4 = "RC4";
            public static final String RC5 = "RC5";
            public static final String RSA = "RSA";

            public static String pbeWithDigestAndEncryption(String digest, String encryption) {
                return "PBEWith" + digest + "And" + encryption;
            }

            public static String pbeWithPrfAndEncryption(String prf, String encryption) {
                return "PBEWith" + prf + "And" + encryption;
            }
        }

        @NoArgsConstructor(access = AccessLevel.NONE)
        public static class Mode {

            public static final String NONE = "NONE";
            public static final String CBC = "CBC";
            public static final String CCM = "CCM";
            public static final String CFB = "CFB";
            public static final String CTR = "CTR";
            public static final String CTS = "CTS";
            public static final String ECB = "ECB";
            public static final String GCM = "GCM";
            public static final String KW = "KW";
            public static final String KWP = "KWP";
            public static final String OFB = "OFB";
            public static final String PCBC = "PCBC";
        }

        @NoArgsConstructor(access = AccessLevel.NONE)
        public static class Padding {

            public static final String NO_PADDING = "NoPadding";
            public static final String ISO10126_PADDING = "ISO10126Padding";
            public static final String OAEP_PADDING = "OAEPPadding";
            public static final String PKCS1_PADDING = "PKCS1Padding";
            public static final String PKCS5_PADDING = "PKCS5Padding";
            public static final String SSL3_PADDING = "SSL3Padding";

            public static String oaepWith(String digest, String mgf) {
                return "OAEPWith" + digest + "And" + mgf + "Padding";
            }
        }
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class Mac {

        public static final String HMAC_MD5 = "HmacMD5";
        public static final String HMAC_SHA1 = "HmacSHA1";
        public static final String HMAC_SHA224 = "HmacSHA224";
        public static final String HMAC_SHA256 = "HmacSHA256";
        public static final String HMAC_SHA384 = "HmacSHA384";
        public static final String HMAC_SHA512 = "HmacSHA512";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class KeyFactory {

        public static final String DIFFIE_HELLMAN = "DiffieHellman";
        public static final String DSA = "DSA";
        public static final String EC = "EC";
        public static final String RSA = "RSA";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class KeyPairGenerator {

        public static final String DIFFIE_HELLMAN = "DiffieHellman";
        public static final String DSA = "DSA";
        public static final String EC = "EC";
        public static final String RSA = "RSA";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class Signature {

        public static final String MD2withRSA = "MD2withRSA";
        public static final String MD5withRSA = "MD5withRSA";
        public static final String SHA1withDSA = "SHA1withDSA";
        public static final String SHA1withRSA = "SHA1withRSA";
        public static final String SHA224withDSA = "SHA224withDSA";
        public static final String SHA256withDSA = "SHA256withDSA";
        public static final String SHA224withRSA = "SHA224withRSA";
        public static final String SHA256withRSA = "SHA256withRSA";
        public static final String SHA384withRSA = "SHA384withRSA";
        public static final String SHA512withRSA = "SHA512withRSA";
        public static final String SHA512_224withRSA = "SHA512/224withRSA";
        public static final String SHA512_256withRSA = "SHA512/256withRSA";
        public static final String SHA1withECDSA = "SHA1withECDSA";
        public static final String SHA224withECDSA = "SHA224withECDSA";
        public static final String SHA256withECDSA = "SHA256withECDSA";
        public static final String SHA384withECDSA = "SHA384withECDSA";
        public static final String SHA512withECDSA = "SHA512withECDSA";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class KeyAgreement {

        public static final String DIFFIE_HELLMAN = "DiffieHellman";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class SecureRandom {

        public static final String SHA1_PRNG = "SHA1PRNG";
        public static final String NATIVE_PRNG = "NativePRNG";
        public static final String NATIVE_PRNG_BLOCKING = "NativePRNGBlocking";
        public static final String NATIVE_PRNG_NON_BLOCKING = "NativePRNGNonBlocking";
        public static final String DRBG = "DRBG";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public static final class KeyGenerator {

        public static final String AES = "AES";
        public static final String ARCFOUR = "ARCFOUR";
        public static final String BLOWFISH = "Blowfish";
        public static final String CHACHA20 = "ChaCha20";
        public static final String CHACHA20_POLY1305 = "ChaCha20-Poly1305";
        public static final String DES = "DES";
        public static final String DESEDE = "DESede";
        public static final String HMAC_MD5 = "HmacMD5";
        public static final String HMAC_SHA1 = "HmacSHA1";
        public static final String HMAC_SHA224 = "HmacSHA224";
        public static final String HMAC_SHA256 = "HmacSHA256";
        public static final String HMAC_SHA384 = "HmacSHA384";
        public static final String HMAC_SHA512 = "HmacSHA512";
        public static final String HMAC_SHA512_224 = "HmacSHA512/224";
        public static final String HMAC_SHA512_256 = "HmacSHA512/256";
        public static final String HMAC_SHA3_224 = "HmacSHA3-224";
        public static final String HMAC_SHA3_256 = "HmacSHA3-256";
        public static final String HMAC_SHA3_384 = "HmacSHA3-384";
        public static final String HMAC_SHA3_512 = "HmacSHA3-512";
        public static final String RC2 = "RC2";
    }

    @NoArgsConstructor(access = AccessLevel.NONE)
    public final class SecretKeyFactory {

        public static final String AES = "AES";
        public static final String ARCFOUR = "ARCFOUR";
        public static final String CHACHA20 = "ChaCha20";
        public static final String CHACHA20_POLY1305 = "ChaCha20-Poly1305";
        public static final String DES = "DES";
        public static final String DESEDE = "DESede";

        public static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES";
        public static final String PBE_WITH_HMACSHA256_AND_AES_128 = "PBEWithHmacSHA256AndAES_128";
        public static final String PBKDF2_WITH_HMACSHA1 = "PBKDF2WithHmacSHA1";
        public static final String PBKDF2_WITH_HMACSHA256 = "PBKDF2WithHmacSHA256";
        public static final String PBKDF2_WITH_HMACSHA512 = "PBKDF2WithHmacSHA512";

        public static String pbeWithDigestAndEncryption(String digest, String encryption) {
            return "PBEWith" + digest + "And" + encryption;
        }

        public static String pbeWithPrfAndEncryption(String prf, String encryption) {
            return "PBEWith" + prf + "And" + encryption;
        }

        public static String pbkdf2WithPrf(String prf) {
            return "PBKDF2With" + prf;
        }
    }


}
