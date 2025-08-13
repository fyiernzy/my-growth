package com.ifast.ipaymy.backend.util.observability.config;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResponseTraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // Get current span and extract trace ID
        Optional.ofNullable(Span.current())
                .map(Span::getSpanContext)
                .map(SpanContext::getTraceId)
                .filter(str -> !str.isBlank())
                .ifPresent(traceId -> httpResponse.setHeader("X-Trace-Id", traceId));

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
