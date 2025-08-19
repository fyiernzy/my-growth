package com.ifast.ipaymy.backend.util.observability.threadpool;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@RequiredArgsConstructor
public class TracingThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private final ThreadPoolTaskExecutor delegate;

    @Override
    public void execute(@NonNull Runnable task) {
        delegate.execute(withContext(Context.current(), task));
    }

    @Override
    @NonNull
    public Future<?> submit(@NonNull Runnable task) {
        return delegate.submit(withContext(Context.current(), task));
    }

    @Override
    @NonNull
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return delegate.submit(withContext(Context.current(), task));
    }

    @Override
    public void setTaskDecorator(@NonNull TaskDecorator taskDecorator) {
        TaskDecorator combinedDecorator = task -> {
            Runnable decoratedTask = taskDecorator.decorate(task);
            return withContext(Context.current(), decoratedTask);
        };
        delegate.setTaskDecorator(combinedDecorator);
    }


    private Runnable withContext(Context context, Runnable task) {
        /*
         * IMPORTANT:
         * Capture the OpenTelemetry Context before entering this method.
         * Capturing it here would miss the active trace, causing traceId/spanId
         * to be lost during propagation.
         */
        return () -> {
            try (Scope ignored = context.makeCurrent()) {
                task.run();
            }
        };
    }

    private <T> Callable<T> withContext(Context context, Callable<T> task) {
        /*
         * IMPORTANT:
         * Capture the OpenTelemetry Context before entering this method.
         * Capturing it here would miss the active trace, causing traceId/spanId
         * to be lost during propagation.
         */
        return () -> {
            try (Scope ignored = context.makeCurrent()) {
                return task.call();
            }
        };
    }

    @Override
    public void setThreadFactory(ThreadFactory threadFactory) {
        delegate.setThreadFactory(threadFactory);
    }

    @Override
    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        delegate.setRejectedExecutionHandler(rejectedExecutionHandler);
    }

    @Override
    public int getCorePoolSize() {
        return delegate.getCorePoolSize();
    }

    @Override
    public void setCorePoolSize(int corePoolSize) {
        delegate.setCorePoolSize(corePoolSize);
    }

    @Override
    public int getMaxPoolSize() {
        return delegate.getMaxPoolSize();
    }

    @Override
    public void setMaxPoolSize(int maxPoolSize) {
        delegate.setMaxPoolSize(maxPoolSize);
    }

    @Override
    public int getKeepAliveSeconds() {
        return delegate.getKeepAliveSeconds();
    }

    @Override
    public void setKeepAliveSeconds(int keepAliveSeconds) {
        delegate.setKeepAliveSeconds(keepAliveSeconds);
    }

    @Override
    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        delegate.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
    }

    @Override
    public void initialize() {
        delegate.initialize();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

    @Override
    @NonNull
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return delegate.getThreadPoolExecutor();
    }

    @Override
    public int getPoolSize() {
        return delegate.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return delegate.getActiveCount();
    }

    @Override
    public int getQueueCapacity() {
        return delegate.getQueueCapacity();
    }

    @Override
    public void setQueueCapacity(int queueCapacity) {
        delegate.setQueueCapacity(queueCapacity);
    }

    @Override
    public void setPrestartAllCoreThreads(boolean prestartAllCoreThreads) {
        delegate.setPrestartAllCoreThreads(prestartAllCoreThreads);
    }

    @Override
    public void setStrictEarlyShutdown(boolean defaultEarlyShutdown) {
        delegate.setStrictEarlyShutdown(defaultEarlyShutdown);
    }

    @Override
    public int getQueueSize() {
        return delegate.getActiveCount();
    }
}
