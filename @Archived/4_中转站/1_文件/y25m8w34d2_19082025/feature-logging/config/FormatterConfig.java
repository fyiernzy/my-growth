package com.ifast.ipaymy.backend.util.observability.config;

import com.ifast.ipaymy.backend.util.constant.SharedConstant;
import com.ifast.ipaymy.backend.util.observability.formatter.OpenTelemetrySqlExporter;
import com.ifast.ipaymy.backend.util.observability.formatter.PrettySqlFormat;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.Slf4JLogger;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FormatterConfig {

    @Bean
    @Profile("!" + SharedConstant.LOCAL)
    public AutoConfigurationCustomizerProvider openTelemetrySqlFormatter() {
        return customizer -> customizer.addTracerProviderCustomizer((builder, config) ->
            builder.addSpanProcessor(SimpleSpanProcessor.create(OpenTelemetrySqlExporter.create()))
        );
    }

    @Bean
    @Profile(SharedConstant.LOCAL)
    public PrettySqlFormat p6spySqlFormatter() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(PrettySqlFormat.class.getName());
        P6SpyOptions.getActiveInstance().setAppender(Slf4JLogger.class.getName());
        P6LogOptions.getActiveInstance()
            .setExcludecategories("info,debug,result,resultset,batch,commit,rollback");
        return new PrettySqlFormat();
    }
}