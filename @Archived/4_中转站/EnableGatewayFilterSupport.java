package com.ifast.ipaymy.apigateway.util.filter.gateway;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
@Target(value = {TYPE})
@Documented
@Import(GatewayFilterConfig.class)
public @interface EnableGatewayFilterSupport {

}
