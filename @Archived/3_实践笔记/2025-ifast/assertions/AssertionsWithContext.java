package com.example.demo.assertions;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Supplier;

// Can be fluent or overloaded
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertionsWithContext {

    private final boolean condition;
    private final Supplier<? extends IfastPayException> exception;
    private Supplier<String> logMessage;
    private Map<String, Object> context;

    public static AssertionsWithContext validateOn(boolean condition,
                                                   Supplier<? extends IfastPayException> exception) {
        return new AssertionsWithContext(condition, exception);
    }

    public AssertionsWithContext withLogMessage(Supplier<String> logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    public AssertionsWithContext withContext(ErrorContext errorContext) {
        this.context = errorContext.toMap();
        return this;
    }

    public void makeAssertion() {
        if (!condition) {
            if (logMessage != null) {
                String message = logMessage.get();
                log.error(message);
            }
            IfastPayException ex = exception.get();
            ex.setContext(context);
            throw ex;
        }
    }
}
