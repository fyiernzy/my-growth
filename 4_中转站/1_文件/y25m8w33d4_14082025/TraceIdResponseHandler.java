package com.example.demo.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TraceIdResponseHandler implements ObservationHandler<ServerRequestObservationContext> {

    private final Tracer tracer;

    @Override
    public void onStop(@NonNull ServerRequestObservationContext ctx) {
        Span span = tracer.currentSpan();
        Optional.ofNullable(span)
            .map(Span::context)
            .map(TraceContext::traceId)
            .filter(traceId -> !traceId.isBlank())
            .ifPresent(traceId -> {
                Object resp = ctx.getResponse();
                if (resp instanceof HttpServletResponse httpServletResponse) {
                    httpServletResponse.setHeader("X-Trace-Id", traceId);
                }
            });
    }

    @Override
    public boolean supportsContext(@NonNull Observation.Context context) {
        return context instanceof ServerRequestObservationContext;
    }
}

