package com.ifast.ipaymy.backend.util.observability.threadpool;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Order
@Component
@RequiredArgsConstructor
public class TracingThreadPoolBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName)
        throws BeansException {
        if (bean instanceof ThreadPoolTaskExecutor original) {
            return new TracingThreadPoolTaskExecutor(original);
        }
        return bean;
    }
}
