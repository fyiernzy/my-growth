package com.ifast.ipaymy.backend.util.cryptographic;

import com.ifast.ipaymy.backend.util.cryptographic.constants.SecurityConstants;
import com.ifast.ipaymy.backend.util.cryptographic.exception.CryptographicException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class RsaKeyUtils {

    public static final int DEFAULT_RSA_KEY_PAIR_SIZE = 3072;

    public static RSAKey generateRSAKey() {
        try {
            KeyPair keyPair = generateRSAKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            return buildRsaKey(publicKey, privateKey);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static KeyPair generateRSAKeyPair() {
        return CryptoUtils.generateKeyPair(SecurityConstants.KeyPairGenerator.RSA,
            new RSAKeyGenParameterSpec(DEFAULT_RSA_KEY_PAIR_SIZE, RSAKeyGenParameterSpec.F4));
    }

    public static RSAKey buildRsaKey(RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey)
        throws JOSEException {
        RSAKey base = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey)
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.RS256).build();
        String kid = base.computeThumbprint().toString();
        return new RSAKey.Builder(base).keyID(kid).build();
    }

    public static RSAKey buildRsaKey(byte[] pkcs8) {
        try {
            RSAPrivateKey privateKey = derivePrivateKey(pkcs8);
            RSAPublicKey publicKey = derivePublicKey(privateKey);
            return buildRsaKey(publicKey, privateKey);
        } catch (Exception e) {
            throw new CryptographicException(e.getMessage(), e);
        }
    }

    private static RSAPrivateKey derivePrivateKey(byte[] bytes) throws GeneralSecurityException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityConstants.KeyFactory.RSA);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    private static RSAPublicKey derivePublicKey(RSAPrivateKey privateKey)
        throws GeneralSecurityException {
        RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey) privateKey;
        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(crtKey.getModulus(),
            crtKey.getPublicExponent());
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityConstants.KeyFactory.RSA);
        return (RSAPublicKey) keyFactory.generatePublic(publicSpec);
    }
}
