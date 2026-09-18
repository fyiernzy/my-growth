package com.ifast.ipaymy.apigateway.util.filter.gateway.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LogSampleGatewayFilterFactory extends
    AbstractGatewayFilterFactory<LogSampleGatewayFilterFactory.Config> {

    public LogSampleGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("A sample GatewayFilterFactory with arg1={}, arg2={}",
                config.arg1(), config.arg2());
            return chain.filter(exchange);
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(Config.ARGUMENT_1, Config.ARGUMENT_2);
    }

    public record Config(
        String arg1,
        String arg2
    ) {

        public static final String ARGUMENT_1 = "arg1";
        public static final String ARGUMENT_2 = "arg2";
    }
}
