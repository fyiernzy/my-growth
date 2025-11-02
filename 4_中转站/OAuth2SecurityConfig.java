package com.ifast.ipaymy.oauth2.config.security;

import com.ifast.ipaymy.backend.util.cryptographic.CryptoUtils;
import com.ifast.ipaymy.backend.util.cryptographic.RsaKeyUtils;
import com.ifast.ipaymy.oauth2.config.security.handler.OAuth2AccessDefinedHandler;
import com.ifast.ipaymy.oauth2.config.security.handler.OAuth2AuthenticationEntryPoint;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerMetadataEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Configuration
public class OAuth2SecurityConfig {

    private static final String DISABLED = "__disabled";
    private static final String DISABLED_ENDPOINT = "https://"
                                                    + DISABLED
                                                    + "/this-endpoint-has-been-disabled";
    private static final String UNSUPPORTED = "UNSUPPORTED";

    @Bean
    public SecurityFilterChain oAuth2SecurityFilterChain(
        HttpSecurity http,
        AuthorizationServerSettings authorizationServerSettings,
        RegisteredClientRepository registeredClientRepository,
        Customizer<OAuth2AuthorizationServerMetadataEndpointConfigurer> authorizationServerMetadataEndpointCustomizer,
        OAuth2AccessDefinedHandler oAuth2AccessDeniedHandler,
        OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint
    ) throws Exception {
        OAuth2AuthorizationServerConfigurer authServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();
        return http
            .securityMatcher(authServerConfigurer.getEndpointsMatcher())
            .with(authServerConfigurer, authServer -> authServer
                .authorizationServerSettings(authorizationServerSettings)
                .registeredClientRepository(registeredClientRepository)
                .authorizationServerMetadataEndpoint(authorizationServerMetadataEndpointCustomizer)
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(disabled("/**")).denyAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(oAuth2AuthenticationEntryPoint)
                .accessDeniedHandler(oAuth2AccessDeniedHandler)
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .build();
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        /*
         * Restrict the access to the following endpoints
         * Use the defaultSettings from Spring Authorization Server (SAS) to
         * avoid hard coding.
         */
        AuthorizationServerSettings defaults = AuthorizationServerSettings.builder().build();
        return AuthorizationServerSettings.builder()
            // Use default options
            .tokenEndpoint(defaults.getTokenEndpoint())
            .tokenIntrospectionEndpoint(defaults.getTokenIntrospectionEndpoint())
            .jwkSetEndpoint(defaults.getJwkSetEndpoint())
            // Disable following options
            .authorizationEndpoint(disabled(defaults.getAuthorizationEndpoint()))
            .deviceAuthorizationEndpoint(disabled(defaults.getDeviceAuthorizationEndpoint()))
            .deviceVerificationEndpoint(disabled(defaults.getDeviceVerificationEndpoint()))
            .tokenRevocationEndpoint(disabled(defaults.getTokenEndpoint()))
            .build();
    }

    @Bean
    Customizer<OAuth2AuthorizationServerMetadataEndpointConfigurer> authorizationServerMetadataEndpointCustomizer() {
        /*
         * Change the /.well-known/oauth-authorization-server configuration
         * to block further all the possible access
         */
        return metadataEndpointConfigurer -> metadataEndpointConfigurer
            .authorizationServerMetadataCustomizer(builder -> builder
                .grantTypes(keepOnly(
                    AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()
                ))
                .tokenEndpointAuthenticationMethods(keepOnly(
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()
                ))
                .tokenIntrospectionEndpointAuthenticationMethods(keepOnly(
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()
                ))
                .authorizationEndpoint(DISABLED_ENDPOINT)
                .deviceAuthorizationEndpoint(DISABLED_ENDPOINT)
                .tokenRevocationEndpoint(DISABLED_ENDPOINT)
                .tokenRevocationEndpointAuthenticationMethods(unsupported())
                .codeChallengeMethods(unsupported())
                .tlsClientCertificateBoundAccessTokens(false)
            );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(@Value("${jwk.path}") Resource jwkResource,
                                                @Value("${jwk.password}") String jwkPassword)
        throws IOException {
        String encryptedRsaKey = jwkResource.getContentAsString(StandardCharsets.UTF_8);
        byte[] pkcs8 = CryptoUtils.decryptToBytes(encryptedRsaKey, jwkPassword.toCharArray());
        return new ImmutableJWKSet<>(new JWKSet(RsaKeyUtils.buildRsaKey(pkcs8)));
    }

    private String disabled(String uri) {
        return DISABLED + uri;
    }

    private Consumer<List<String>> keepOnly(String... choices) {
        return list -> {
            list.clear();
            list.addAll(Arrays.stream(choices).toList());
        };
    }

    private Consumer<List<String>> unsupported() {
        return list -> {
            list.clear();
            list.add(UNSUPPORTED);
        };
    }
}
