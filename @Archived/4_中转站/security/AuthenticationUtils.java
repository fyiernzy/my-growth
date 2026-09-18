package com.ifast.ipaymy.backend.util.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class AuthenticationUtils {

    public static @NonNull Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public static @Nullable Authentication getAuthenticationOrNull() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static @NonNull Optional<Object> getPrincipal() {
        return getAuthentication().map(Authentication::getPrincipal);
    }

    public static @Nullable Object getPrincipalOrNull() {
        return getPrincipal().orElse(null);
    }

    public static @NonNull <T> Optional<T> getPrincipal(@NonNull Class<T> targetType) {
        return getPrincipal().filter(targetType::isInstance).map(targetType::cast);
    }

    public static @Nullable <T> T getPrincipalOrNull(@NonNull Class<T> targetType) {
        return getPrincipal(targetType).orElse(null);
    }

    public static @NonNull Optional<Object> getDetails() {
        return getAuthentication().map(Authentication::getDetails);
    }

    public static @Nullable Object getDetailsOrNull() {
        return getDetails().orElse(null);
    }

    public static @NonNull <T> Optional<T> getDetails(@NonNull Class<T> targetType) {
        return getDetails().filter(targetType::isInstance).map(targetType::cast);
    }

    public static @Nullable <T> T getDetailsOrNull(@NonNull Class<T> targetType) {
        return getDetails(targetType).orElse(null);
    }

    public static @Nullable String getUsernameOrNull() {
        return getAuthentication().map(Authentication::getName).orElse(null);
    }

    public static @NonNull Set<String> getAuthorityNames() {
        return getAuthentication()
            .map(Authentication::getAuthorities)
            .map(authorities -> authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet())
            )
            .orElseGet(Set::of);
    }

    public static boolean hasAnyAuthority(@NonNull String... authorities) {
        return hasAnyAuthority(Set.of(authorities));
    }

    public static boolean hasAnyAuthority(@NonNull Set<String> authorities) {
        return getAuthorityNames().stream().anyMatch(authorities::contains);
    }

    public static boolean hasAllAuthority(@NonNull String... authorities) {
        return hasAllAuthority(Set.of(authorities));
    }

    public static boolean hasAllAuthority(@NonNull Set<String> authorities) {
        return authorities.containsAll(getAuthorityNames());
    }

    public static boolean isAuthenticated() {
        return getAuthentication().map(Authentication::isAuthenticated).orElse(false);
    }
}
