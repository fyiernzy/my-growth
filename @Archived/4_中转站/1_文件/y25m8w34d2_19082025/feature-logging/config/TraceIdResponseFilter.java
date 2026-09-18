package com.ifast.ipaymy.backend.util.observability.config;

import com.ifast.ipaymy.backend.util.validator.ValidatorUtils;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TraceIdResponseFilter extends OncePerRequestFilter {
    private final Tracer tracer;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Optional.ofNullable(tracer.currentSpan())
                .map(Span::context)
                .map(TraceContext::traceId)
                .filter(ValidatorUtils::isNotBlank)
                .ifPresent(traceId -> response.setHeader("X-Trace-Id", traceId));
        filterChain.doFilter(request, response);
    }
}
