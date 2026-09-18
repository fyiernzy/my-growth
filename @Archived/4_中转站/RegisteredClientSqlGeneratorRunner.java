package com.ifast.ipaymy.oauth2.runner.oauth2;

import com.ifast.ipaymy.oauth2.utils.oauth2.RegisteredClientSqlGenerator;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class RegisteredClientSqlGeneratorRunner {

    @SneakyThrows
    public static void main(String[] args) {
        record SimplifiedOAuth2RegisteredClient(
            String clientId,
            String encryptedClientSecret,
            Set<String> scope
        ) {

        }

        Stream.of(
                new SimplifiedOAuth2RegisteredClient(
                    "x-my-batch-ws",
                    "$2a$10$dA1gPJA/s2o6kU9f0edbqeN/OYRf6YFA/AVj73GFhUKVq.cKFQmdK",
                    Set.of()
                ),
                new SimplifiedOAuth2RegisteredClient(
                    "x-my-modular-ws",
                    "$2a$10$wtWI0SdvXM5ZNhsbco.wee4uyEe.m0VoO1sO40DgPKJe5hJkvMgiy",
                    Set.of()
                ),
                new SimplifiedOAuth2RegisteredClient(
                    "x-my-internal-ws",
                    "$2a$10$st70jptP2Li9.oYA3R8wP.TBcxUsT1VzM578u5IdNk9.6Ba.L980G",
                    Set.of()
                ),
                new SimplifiedOAuth2RegisteredClient(
                    "x-my-public-ws",
                    "$2a$10$q.iTlbVRSZp0E5LYlX0IguhgGkqe1G06iOw8oEGG9IuBBnfAZ6tua",
                    Set.of()
                ),
                new SimplifiedOAuth2RegisteredClient(
                    "x-my-web-backoffice",
                    "$2a$10$EhFd7m8YSpgdDFeUgxp7pOiehWRwN3/0nKebX3JqOKF5UNJSH3g6m",
                    Set.of()
                ),
                new SimplifiedOAuth2RegisteredClient(
                    "x-my-web-ifast-pay",
                    "$2a$10$6ur0yAkdZeVFkfpGO7RKDuRwdvkdN2qFIWuazIiWqYFS9hVgluOW2",
                    Set.of()
                ),
                new SimplifiedOAuth2RegisteredClient(
                    "x-test-postman",
                    "$2a$10$AM93/tXtZVEqZrNLvw57/uPLS0103i8.RA.p1vE6G3vOOWcjpGt9K",
                    Set.of()
                )
            )
            .map(client -> registeredClient(
                    String.valueOf(UUID.randomUUID()),
                    client.clientId(),
                    client.encryptedClientSecret(),
                    client.scope()
                )
            )
            .map(client -> {
                try {
                    return RegisteredClientSqlGenerator.generate(client);
                } catch (Exception ignored) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .forEach(System.out::println);
    }

    private static RegisteredClient registeredClient(String id,
                                                     String clientId,
                                                     String clientSecret,
                                                     Set<String> scopes) {
        return RegisteredClient.withId(id)
            .clientId(clientId)
            .clientName(clientId)
            .clientSecret(clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .tokenSettings(TokenSettings.builder().build())
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scopes(consumer -> consumer.addAll(scopes))
            .build();
    }
}
