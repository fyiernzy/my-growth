package com.ifast.ipaymy.backend.util.observability.quartz;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class OtelJobListener implements JobListener {

    public static final String QUARTZ_JOB_PREFIX = "quartz.job:";
    private static final String SPAN = "span";
    private static final String SCOPE = "scope";
    /*
     * For compatibility as different vendor might use different labels
     */
    private static final String TRACE_ID_LABEL_1 = "traceId";
    private static final String TRACE_ID_LABEL_2 = "trace_id";
    private static final String SPAN_ID_LABEL_1 = "spanId";
    private static final String SPAN_ID_LABEL_2 = "span_id";
    private final Tracer tracer;

    public OtelJobListener(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public String getName() {
        return "otel-job-listener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        Span currentSpan = Span.current();
        log.info("Before creating job span - Current span valid: {}, traceId: {}",
            currentSpan.getSpanContext().isValid(),
            currentSpan.getSpanContext().getTraceId());

        Span span = tracer
            .spanBuilder(QUARTZ_JOB_PREFIX + jobExecutionContext.getJobDetail().getKey())
            .startSpan();

        Scope scope = span.makeCurrent();

        jobExecutionContext.put(SPAN, span);
        jobExecutionContext.put(SCOPE, scope);

        log.info("Created job span - traceId: {}, spanId: {}",
            span.getSpanContext().getTraceId(),
            span.getSpanContext().getSpanId());

        log.debug("Started span for job: {} with traceId: {}",
            jobExecutionContext.getJobDetail().getKey(),
            span.getSpanContext().getTraceId());

        SpanContext spanContext = span.getSpanContext();
        MDC.put(TRACE_ID_LABEL_1, spanContext.getTraceId());
        MDC.put(TRACE_ID_LABEL_2, spanContext.getTraceId());
        MDC.put(SPAN_ID_LABEL_1, spanContext.getSpanId());
        MDC.put(SPAN_ID_LABEL_2, spanContext.getSpanId());

        Span verifySpan = Span.current();
        log.info("Verified current span - traceId: {}",
            verifySpan.getSpanContext().getTraceId());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext,
                               JobExecutionException jobExecutionException) {
        MDC.remove(TRACE_ID_LABEL_1);
        MDC.remove(TRACE_ID_LABEL_2);
        MDC.remove(SPAN_ID_LABEL_1);
        MDC.remove(SPAN_ID_LABEL_2);

        // 1. Close the scope
        Scope scope = (Scope) jobExecutionContext.get(SCOPE);
        if (scope != null) {
            scope.close();
        }

        // 2. Close the span
        Span span = (Span) jobExecutionContext.get(SPAN);
        if (span != null) {
            if (jobExecutionException != null) {
                span.recordException(jobExecutionException);
                span.setStatus(StatusCode.ERROR);
            }
            span.end();
        }
    }


    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        // do nothing
    }
}

