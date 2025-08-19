package com.ifast.ipaymy.backend.util.observability.helpers;

import com.ifast.ipaymy.backend.util.constant.SharedConstant;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Utility class for retrieving the current {@code traceId} and {@code spanId}
 * from the active OpenTelemetry {@link io.opentelemetry.api.trace.Span}.
 * <p>
 * These identifiers are used for distributed tracing, allowing logs and telemetry
 * data to be correlated across services and threads. Since {@code Span.current()}
 * is bound to the calling thread, the {@code traceId} and {@code spanId} should be
 * obtained <b>before</b> submitting work to another thread or worker pool. Failing
 * to do so may result in invalid or empty identifiers, causing broken trace chains
 * and making it difficult to correlate asynchronous operations.
 * </p>
 *
 * <p>
 * <b>Good Example</b>
 * <pre>{@code
 * // Capture traceId and spanId on the current thread
 * String traceId = TracingUtil.getTraceId();
 * String spanId  = TracingUtil.getSpanId();
 *
 * // Pass them along explicitly to the worker thread
 * executorService.submit(() -> {
 *     log.info("traceId={}, spanId={}", traceId, spanId);
 *     processRequest();
 * });
 * }</pre>
 * <p><b>Result:</b> The worker thread logs contain the correct traceId/spanId,
 * and the operation can be correlated across distributed systems.</p>
 *
 * <p>
 * <b>Bad Example</b>
 * <pre>{@code
 * // Directly calling TracingUtil in the worker thread
 * executorService.submit(() -> {
 *     log.info("traceId={}, spanId={}", TracingUtil.getTraceId(), TracingUtil.getSpanId());
 *     processRequest();
 * });
 * }</pre>
 * <p><b>Result:</b> The worker thread does not have access to the original context.
 * {@code traceId} and {@code spanId} will be empty or invalid, breaking trace
 * correlation and making logs harder to follow.</p>
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public class TracingUtil {

    public static String getTraceId() {
        return Optional.ofNullable(Span.current())
            .map(Span::getSpanContext)
            .map(SpanContext::getTraceId)
            .orElse(SharedConstant.EMPTY_STRING);
    }

    public static String getSpanId() {
        return Optional.ofNullable(Span.current())
            .map(Span::getSpanContext)
            .map(SpanContext::getSpanId)
            .orElse(SharedConstant.EMPTY_STRING);
    }
}
