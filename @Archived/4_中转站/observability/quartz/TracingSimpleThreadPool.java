package com.ifast.ipaymy.backend.util.observability.quartz;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerConfigException;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.ThreadPool;

/**
 * Custom Quartz ThreadPool that propagates OpenTelemetry context to all threads.
 * This ensures that database calls made by Quartz's internal SchedulerThread
 * share the same trace context as the job execution.
 */
@Slf4j
public class TracingSimpleThreadPool implements ThreadPool {

    private ThreadPool delegate;

    public TracingSimpleThreadPool() {
        this.delegate = new SimpleThreadPool();
    }

    @Override
    public boolean runInThread(Runnable runnable) {
        Context context = Context.current();

        return delegate.runInThread(() -> {
            try (Scope ignored = context.makeCurrent()) {
                runnable.run();
            }
        });
    }

    @Override
    public int blockForAvailableThreads() {
        return delegate.blockForAvailableThreads();
    }

    @Override
    public void initialize() throws SchedulerConfigException {
        delegate.initialize();
    }

    @Override
    public void shutdown(boolean waitForJobsToComplete) {
        delegate.shutdown(waitForJobsToComplete);
    }

    @Override
    public int getPoolSize() {
        return delegate.getPoolSize();
    }

    @Override
    public void setInstanceId(String schedInstId) {
        delegate.setInstanceId(schedInstId);
    }

    @Override
    public void setInstanceName(String schedName) {
        delegate.setInstanceName(schedName);
    }
}
