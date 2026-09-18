package com.ifast.ipaymy.oauth2.runner.security;

import com.ifast.ipaymy.backend.util.cryptographic.CryptoUtils;
import com.ifast.ipaymy.backend.util.cryptographic.RsaKeyUtils;
import com.ifast.ipaymy.backend.util.json.JsonUtils;
import com.ifast.ipaymy.oauth2.utils.utils.FileUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;


class JwkGeneratorRunner {

    // Must match application.yaml
    private static final String JWK_PATH = "src/main/resources/security/keys/rsa-jwt-signing.key.enc";
    private static final String JWK_PASSWORD = "secret";

    private static final Path KEY_PATH = FileUtils.getSubprojectRoot(JwkGeneratorRunner.class)
        .resolve(JWK_PATH);

    public static void main(String[] args) throws Exception {
        FileUtils.createParentPathIfNotExist(KEY_PATH);
        System.out.println("Encrypted RSA Private Key Path: " + KEY_PATH.toAbsolutePath());
        JWKSet jwkSet = new JWKSet(resolveRsaKey().toPublicJWK());
        System.out.printf("""
            Public JWK Set:
            %s
            %n""", JsonUtils.pretty(jwkSet.toString())
        );
    }

    private static RSAKey resolveRsaKey() throws Exception {
        return Files.exists(KEY_PATH)
               ? loadRsaKeyFromFile()
               : createAndPersistNewRsaKey();
    }

    private static RSAKey loadRsaKeyFromFile()
        throws IOException {
        String encryptedRsaKey = Files.readString(KEY_PATH, StandardCharsets.UTF_8);
        byte[] pkcs8 = CryptoUtils.decryptToBytes(encryptedRsaKey, JWK_PASSWORD.toCharArray());
        return RsaKeyUtils.buildRsaKey(pkcs8);
    }

    private static RSAKey createAndPersistNewRsaKey() throws Exception {
        RSAKey rsaKey = RsaKeyUtils.generateRSAKey();
        persistEncryptedPrivateKey(rsaKey);
        System.out.println("Generated new RSAKey.");
        return rsaKey;
    }

    private static void persistEncryptedPrivateKey(RSAKey rsaKey)
        throws Exception {
        PrivateKey privateKey = rsaKey.toPrivateKey();
        byte[] pkcs8 = privateKey.getEncoded();
        String encoded = CryptoUtils.encrypt(pkcs8, JWK_PASSWORD.toCharArray());
        FileUtils.writeAtomically(KEY_PATH, encoded);
    }
}
