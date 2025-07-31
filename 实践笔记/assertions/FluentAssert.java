package com.example.demo.assertions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

public class FluentAssert {

    public static FluentConsecutiveAssertions withDefaultFallback(ErrorCodeEnum errorCodeEnum) {
        return new FluentConsecutiveAssertions(
            () -> new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage()));
    }

    public static FluentConsecutiveAssertions withDefaultFallback(Supplier<? extends IfastPayException> defaultFallback) {
        return new FluentConsecutiveAssertions(defaultFallback);
    }

    public static ConsecutiveAssertions simple() {
        return new ConsecutiveAssertions();
    }

    public static <E extends ErrorCodeEnum> ErrorCodeAssertions<E> withErrorCodeAndMessage(E errorCode,
                                                                                           String message) {
        return new ErrorCodeAssertions<>(errorCode, message);
    }

    public static <E extends IfastPayException> ExceptionAssertions<E> withException(Supplier<E> exception) {
        return new ExceptionAssertions<>(exception);
    }

    public static <E extends ErrorCodeEnum> ErrorCodeAssertions<E> withErrorCode(E errorCodeEnum) {
        return new ErrorCodeAssertions<>(errorCodeEnum);
    }

    public static class ConsecutiveAssertions {

        public ConsecutiveAssertions notNull(Object object,
                                             Supplier<? extends IfastPayException> exception) {
            if (object == null) {
                throw exception.get();
            }
            return this;
        }

        public ConsecutiveAssertions notBlank(String string,
                                              Supplier<? extends IfastPayException> exception) {
            if (ValidatorsUtil.isNotBlank(string)) {
                throw exception.get();
            }
            return this;
        }
    }

    @AllArgsConstructor
    public static class FluentConsecutiveAssertions {

        private Supplier<? extends IfastPayException> exception;

        public FluentConsecutiveAssertions notNull(Object object) {
            if (object == null) {
                throw exception.get();
            }
            return this;
        }

        public FluentConsecutiveAssertions withException(Supplier<? extends IfastPayException> exception) {
            this.exception = exception;
            return this;
        }

        public FluentConsecutiveAssertions withErrorCode(ErrorCodeEnum errorCodeEnum) {
            this.exception = () -> new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage());
            return this;
        }

        public FluentConsecutiveAssertions notBlank(String string) {
            if (exception != null && ValidatorsUtil.isNotBlank(string)) {
                throw exception.get();
            }
            return this;
        }
    }

    public static class ExceptionAssertions<E extends IfastPayException> {

        private final String message;
        private Supplier<E> exception;

        private ExceptionAssertions(Supplier<E> exception) {
            this(exception, null);
        }

        private ExceptionAssertions(Supplier<E> exception, String message) {
            this.exception = exception;
            this.message = message;
        }

        public ExceptionAssertions<E> withException(Supplier<E> exception) {
            if (exception != null) {
                this.exception = exception;
            }
            return this;
        }

        public StringAssertion assertThat(String string) {
            return new StringAssertion(string, exception, message);
        }

        public <T extends Number & Comparable<T>> NumberAssertion<T> assertThat(T number) {
            return new NumberAssertion<>(number, message, exception);
        }
    }

    public static class ErrorCodeAssertions<E extends ErrorCodeEnum> {

        private String message;
        private Supplier<IfastPayException> exception;
        private E errorCodeEnum;

        private ErrorCodeAssertions(E errorCodeEnum) {
            this(errorCodeEnum, null);
        }

        private ErrorCodeAssertions(E errorCodeEnum, String message) {
            this.exception = () -> new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage());
            this.message = message;
        }

        private ErrorCodeAssertions<E> withErrorCode(E errorCodeEnum) {
            this.exception = () -> new IfastPayException(errorCodeEnum.getErrorCode(),
                errorCodeEnum.getErrorMessage());
            return this;
        }

        public StringAssertion assertThat(String string) {
            return new StringAssertion(string, exception, message);
        }

        public <T extends Number & Comparable<T>> NumberAssertion<T> assertThat(T number) {
            return new NumberAssertion<>(number, message, exception);
        }
    }

    @Slf4j
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    abstract static class AbstractAssertion {

        protected final String message;
        protected final Supplier<? extends IfastPayException> exception;

        protected void isTrue(boolean condition) {
            if (!condition) {
                if (ValidatorsUtil.isNotBlank(message)) {
                    log.error(message);
                }
                throw exception.get();
            }
        }
    }

    public static class StringAssertion extends AbstractAssertion {

        private final String string;

        private StringAssertion(String string,
                                Supplier<? extends IfastPayException> exception,
                                String message) {
            super(message, exception);
            this.string = string;
        }

        public StringAssertion isNotBlank() {
            isTrue(ValidatorsUtil.isNotBlank(string));
            return this;
        }
    }

    public static class NumberAssertion<E extends Number & Comparable<E>> extends
        AbstractAssertion {

        private final E value;

        private NumberAssertion(E value,
                                String message,
                                Supplier<? extends IfastPayException> exception) {
            super(message, exception);
            this.value = value;
        }

        public void isSmallerThan(E another) {
            boolean notNull = value != null && another != null;
            isTrue(notNull && value.compareTo(another) < 0);
        }
    }

}
