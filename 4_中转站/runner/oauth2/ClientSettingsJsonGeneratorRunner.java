package com.ifast.ipaymy.oauth2.runner.oauth2;

import com.ifast.ipaymy.oauth2.utils.oauth2.ClientSettingsJsonGenerator;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

public class ClientSettingsJsonGeneratorRunner {

    public static void main(String[] args) throws Exception {
        ClientSettings clientSettings = getClientSettings();
        String json = ClientSettingsJsonGenerator.generate(clientSettings);
        System.out.println(json);
    }

    /*
     * Customize your ClientSettings here.
     * Use ClientSettings.builder().build() for default settings.
     */
    private static ClientSettings getClientSettings() {
        return ClientSettings.builder().build();
    }
}
