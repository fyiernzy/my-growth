package com.example.demo.assertions;

import java.util.function.Supplier;

public class AccountException extends IfastPayException {

    public AccountException(AccountErrorCodeEnum accountErrorCodeEnum) {
        super(accountErrorCodeEnum.getErrorCode(), accountErrorCodeEnum.getErrorMessage(), null);
    }

    public static Supplier<AccountException> supply(AccountErrorCodeEnum accountErrorCodeEnum) {
        return () -> new AccountException(accountErrorCodeEnum);
    }
}
