package com.example.demo.assertions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class IfastPayException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    @Setter(AccessLevel.PUBLIC)
    private Map<String, Object> context;

    public IfastPayException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public IfastPayException(String errorCode, String errorMessage, Map<String, Object> context) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.context = context;
    }
}