package com.ifast.ipaymy.backend.util.observability.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class LoggingUtil {
    /**
     * Temporarily sets the ThreadLocal Mapped Diagnostic Context (MDC) for the current thread,
     * runs the given action, and then restores the previous MDC state.
     *
     * @param mdc      the MDC context to set during execution
     * @param runnable the action to execute with the provided MDC context
     */
    public static void withMdc(Map<String, String> mdc, Runnable runnable) {
        try {
            mdc.forEach(MDC::put);
            runnable.run();
        } finally {
            mdc.keySet().forEach(MDC::remove);
        }
    }
}
