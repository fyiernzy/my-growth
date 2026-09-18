package com.ifast.ipaymy.apigateway.util.filter.global;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class OrderedGlobalFilter implements GlobalFilter, Ordered {

    private final GlobalFilter delegate;
    private final int order;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return delegate.filter(exchange, chain);
    }

    @Override
    public int getOrder() {
        return order;
    }
}
