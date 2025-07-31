package com.example.demo.assertions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class DomainAssert<C extends Enum<C> & ErrorCodeEnum, E extends IfastPayException> {

    private final Function<C, E> exception; // Can remove if want general

    public static <C extends Enum<C> & ErrorCodeEnum, E extends IfastPayException> DomainAssert<C, E> forException(
        Function<C, E> exception) {
        return new DomainAssert<>(exception);
    }

    public static <C extends Enum<C> & ErrorCodeEnum> DomainAssert<C, IfastPayException> defaultAssert() {
        return new DomainAssert<>(code -> new IfastPayException(code.getErrorCode(),
            code.getErrorMessage()));
    }

    public void notBlank(String string, C errorCodeEnum) {
        if (!ValidatorsUtil.isNotBlank(string)) {
            throw new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage());
        }
    }

}
