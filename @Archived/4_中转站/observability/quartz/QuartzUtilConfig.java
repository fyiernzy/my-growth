package com.ifast.ipaymy.backend.util.observability.quartz;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class QuartzUtilConfig {

    @Bean
    SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer(OtelJobListener listener) {
        return factory -> factory.setGlobalJobListeners(listener);
    }

    @Bean
    public String myuopenTelemetry(OpenTelemetry openTelemetry) {
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        return openTelemetry.toString();
    }
}

