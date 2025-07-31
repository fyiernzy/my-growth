package com.example.demo.assertions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountDomainAssert {

    @Bean
    public DomainAssert<AccountErrorCodeEnum, AccountException> accountDomainAssert() {
        return new DomainAssert<>(AccountException::new);
    }

}
