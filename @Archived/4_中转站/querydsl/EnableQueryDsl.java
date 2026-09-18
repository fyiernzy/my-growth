package com.ifast.ipaymy.backend.util.querydsl;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(value = RUNTIME)
@Target(value = {TYPE})
@Documented
@Import({
        QueryDslConfig.class
})
public @interface EnableQueryDsl {
}

