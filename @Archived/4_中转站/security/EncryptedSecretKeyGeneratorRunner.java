package com.ifast.ipaymy.oauth2.runner.security;

import com.ifast.ipaymy.backend.util.codec.Base64Utils;
import com.ifast.ipaymy.backend.util.cryptographic.CryptoUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

class EncryptedSecretKeyGeneratorRunner {

    public static void main(String[] args) {
        SecretKey secretKey = CryptoUtils.generateAES256SecretKey();
        String encodedKey = Base64Utils.encodeToString(secretKey.getEncoded());
        System.out.println("Secret Key             : " + encodedKey);

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println("Encrypted Secret Key   : " + passwordEncoder.encode(encodedKey));

        System.out.println("Share secret key with others, and save encrypted to DB");
    }

}
