package com.example.demo.assertions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FxErrorCodeEnum implements ErrorCodeEnum {
    INVALID_ACCOUNT_FORMAT("A001", "invalid.account.format"),
    INVALID_ACCOUNT_ID("A002", "invalid.account.id");

    private final String errorCode;
    private final String errorMessage;
}
