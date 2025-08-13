package com.example.demo.config;

import org.slf4j.MDC;

import java.util.Map;

public class ObservabilityUtils {

    public static void withMdc(Map<String, String> mdc, Runnable runnable) {
        try {
            mdc.forEach(MDC::put);
            runnable.run();
        } finally {
            mdc.keySet().forEach(MDC::remove);
        }
    }
}
