package com.ifast.ipaymy.oauth2.runner.oauth2;

import com.ifast.ipaymy.oauth2.utils.oauth2.TokenSettingsJsonGenerator;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

public class TokenSettingsJsonGeneratorRunner {

    public static void main(String[] args) throws Exception {
        TokenSettings tokenSettings = getTokenSettings();
        String json = TokenSettingsJsonGenerator.generate(tokenSettings);
        System.out.println(json);
    }

    /*
     * Customize your TokenSettings here.
     * Use TokenSettings.builder().build() for default settings.
     */
    private static TokenSettings getTokenSettings() {
        return TokenSettings.builder().build();
    }
}
