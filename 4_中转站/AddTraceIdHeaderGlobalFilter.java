package com.ifast.ipaymy.apigateway.util.filter.global;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AddTraceIdHeaderGlobalFilter implements GlobalFilter {

    public static final String X_TRACE_ID = "X-Trace-Id";
    private final Tracer tracer;
    private final Propagator propagator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.defer(() -> {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan == null) {
                log.info("Cannot inject X-Trace-Id due to null currentSpan.");
                return chain.filter(exchange);
            }
            String traceId = currentSpan.context().traceId();

            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();

            propagator.inject(currentSpan.context(), requestBuilder,
                (builder, key, value) -> {
                    if (builder != null) {
                        builder.header(key, value);
                    }
                }
            );

            ServerWebExchange updatedExchange = exchange.mutate()
                .request(requestBuilder.build())
                .build();

            updatedExchange.getResponse().beforeCommit(() -> {
                HttpHeaders headers = updatedExchange.getResponse().getHeaders();
                if (!headers.containsKey(X_TRACE_ID)) {
                    headers.add(X_TRACE_ID, traceId);
                }
                return Mono.empty();
            });

            return chain.filter(updatedExchange);
        });
    }
}

