package com.example.demo.assertions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Assertions {

    @Bean
    public ErrorCodeDomainAssert<AccountErrorCodeEnum> AccountAssertions() {
        return new ErrorCodeDomainAssert<>();
    }
}
