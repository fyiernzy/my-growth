package com.example.demo.assertions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Exceptions {

    public static Supplier<AccountException> supply(AccountErrorCodeEnum accountErrorCodeEnum) {
        return () -> new AccountException(accountErrorCodeEnum);
    }

}
