package com.ifast.ipaymy.oauth2.utils.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

/*
 * A generator that generates the TokenSettings JSON to be used in the SQL
 * It allows other class to use by calling TokenSettingsJsonGenerator.generateTokenSettings()
 */
public class TokenSettingsJsonGenerator {


    public static String generate(TokenSettings tokenSettings) throws Exception {
        return getObjectMapper().writeValueAsString(tokenSettings.getSettings());
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = TokenSettingsJsonGenerator.class.getClassLoader();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
