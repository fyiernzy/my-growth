package com.ifast.ipaymy.oauth2.utils.oauth2;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public class RegisteredClientSqlGenerator {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);

    private static final String OAUTH2_REGISTERED_CLIENT_INSERT_SQL = """
        INSERT INTO oauth2_registered_client (
            id,
            client_id,
            client_id_issued_at,
            client_secret,
            client_secret_expires_at,
            client_name,
            client_authentication_methods,
            authorization_grant_types,
            redirect_uris,
            post_logout_redirect_uris,
            scopes,
            client_settings,
            token_settings
        )
        VALUES (
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s,
            %s
        );
        """;


    public static String generate(RegisteredClient registeredClient) throws Exception {
        String id = quoteOrNull(registeredClient.getId());
        String clientId = quoteOrNull(registeredClient.getClientId());
        String issuedAt = toTimestampOrDefault(registeredClient.getClientIdIssuedAt());
        String secret = quoteOrNull(registeredClient.getClientSecret());
        String secretExpiresAt = null;
        String clientName = quoteOrNull(registeredClient.getClientName());
        String authMethods = quoteOrNull(join(
            registeredClient.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.toSet())
        ));
        String grantTypes = quoteOrNull(join(
            registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.toSet())
        ));
        String redirectUris = quoteOrNull(join(registeredClient.getRedirectUris()));
        String logoutUris = quoteOrNull(join(registeredClient.getPostLogoutRedirectUris()));
        String scopes = quoteOrEmpty(join(registeredClient.getScopes()));
        String clientSettingsJson =
            quoteOrNull(ClientSettingsJsonGenerator.generate(
                registeredClient.getClientSettings()));
        String tokenSettingsJson =
            quoteOrNull(TokenSettingsJsonGenerator.generate(
                registeredClient.getTokenSettings()));

        return String.format(OAUTH2_REGISTERED_CLIENT_INSERT_SQL,
            id, clientId, issuedAt, secret, secretExpiresAt, clientName,
            authMethods, grantTypes, redirectUris, logoutUris, scopes,
            clientSettingsJson, tokenSettingsJson);
    }

    private static String join(Set<String> values) {
        return (values == null || values.isEmpty()) ? null : String.join(",", values);
    }

    private static String quoteOrNull(String value) {
        return value == null ? null : "'" + value.replace("'", "''") + "'";
    }

    private static String quoteOrEmpty(String value) {
        return value == null ? "''" : quoteOrNull(value);
    }

    private static String toTimestampOrDefault(java.time.Instant instant) {
        return instant == null
               ? "CURRENT_TIMESTAMP"
               : quoteOrNull(TIMESTAMP_FORMATTER.format(instant));
    }

}
