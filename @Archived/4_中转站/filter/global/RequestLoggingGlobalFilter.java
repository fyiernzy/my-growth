package com.ifast.ipaymy.apigateway.util.filter.global;

import com.ifast.ipaymy.apigateway.util.wrapper.RequestAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class RequestLoggingGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String separator = "-------------------------------------------------------------";
        RequestAccessor request = RequestAccessor.of(exchange);
        log.info("""
            %s
            Route id:             %s
            Incoming request:     %s
            Route request:        %s
            Headers:              %s
            %s
            """.formatted(
            separator,
            request.getRouteId(),
            request.getOriginalURIAsString(),
            request.getRouteURIAsString(),
            maskSensitiveInfo(request.getHeadersAsMap()).toString(),
            separator
        ));
        Mono<Void> filter = chain.filter(exchange);
        try {
            log.info("HTTP Response Status: {}",
                Objects.requireNonNull(exchange.getResponse().getStatusCode()));
            log.info(separator);
        } catch (Exception ex) {
            log.error("Error", ex);
        }
        return filter;
    }

    private Map<String, String> maskSensitiveInfo(Map<String, String> map) {
        Set<String> masked = Set.of(HttpHeaders.AUTHORIZATION);
        return map.keySet().stream()
            .filter(key -> !masked.contains(key))
            .collect(Collectors.toMap(Function.identity(), map::get));
    }

}