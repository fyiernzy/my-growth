package com.example.demo.assertions;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class ErrorContext {

    private final Map<String, Object> context;

    public static ErrorContext with(String key, Object value) {
        return new ErrorContext(new HashMap<>()).thenWith(key, value);
    }

    public ErrorContext thenWith(String key, Object value) {
        context.put(key, value);
        return this;
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(context);
    }

}
