package com.example.demo.assertions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ErrorCodeDomainAssert<C extends Enum<C> & ErrorCodeEnum> {

    public void notBlank(String string, C errorCodeEnum) {
        if (!ValidatorsUtil.isNotBlank(string)) {
            throw new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage());
        }
    }

}
