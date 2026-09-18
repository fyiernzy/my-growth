package com.ifast.ipaymy.modular.config.filter;

import io.micrometer.common.util.StringUtils;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get current span and extract trace ID
        Optional.ofNullable(Span.current())
                .map(Span::getSpanContext)
                .map(SpanContext::getTraceId)
                .filter(StringUtils::isNotBlank)
                .ifPresent(traceId -> httpResponse.setHeader("X-Trace-Id", traceId));

        chain.doFilter(request, response);
    }
}