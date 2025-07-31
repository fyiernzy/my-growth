package com.example.demo.assertions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertionUtil {

    private static final Map<Class<? extends ErrorCodeEnum>, Class<? extends IfastPayException>> MAP;

    static {
        MAP = new HashMap<>();
        MAP.put(AccountErrorCodeEnum.class, AccountException.class);
    }

    public static void notNull(String string, ErrorCodeEnum errorCodeEnum,
                               Map<String, Object> context) {
        if (ValidatorsUtil.isNotBlank(string)) {
            throw new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage(), context);
        }
    }

    public static void notNull(String string, ErrorCodeEnum errorCodeEnum) {
        notNull(string, errorCodeEnum, null);
    }

    public static void notNull(String string, Supplier<? extends IfastPayException> exception) {
        if (ValidatorsUtil.isNotBlank(string)) {
            throw exception.get();
        }
    }

    public static void notNullReflected(String string, ErrorCodeEnum errorCodeEnum) {
        if (ValidatorsUtil.isNotBlank(string)) {
            throwException(errorCodeEnum);
        }
    }

    private static void throwException(ErrorCodeEnum errorCodeEnum) {
        Class<? extends ErrorCodeEnum> errorCodeEnumClass = errorCodeEnum.getClass();
        Class<? extends IfastPayException> exceptionClass = MAP.get(errorCodeEnum.getClass());
        try {
            Constructor<? extends IfastPayException> constructor = exceptionClass.getDeclaredConstructor(
                errorCodeEnumClass);
            throw constructor.newInstance(errorCodeEnum);
        } catch (NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException e) {
            // Fallback mechanism
            throw new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage());
        }
    }
}
