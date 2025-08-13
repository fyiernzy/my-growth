package com.example.demo.config;

import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelExporterConfig {

    @Bean
    public AutoConfigurationCustomizerProvider sqlOnlyPrettyExporter() {
        return customizer -> customizer.addTracerProviderCustomizer((builder, config) ->
            builder.addSpanProcessor(SimpleSpanProcessor.create(OtelSqlExporter.create()))
        );
    }
}
