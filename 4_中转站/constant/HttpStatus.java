package com.ifast.ipaymy.backend.util.constant;

import java.util.Set;

public final class HttpStatus {

    private HttpStatus() {}

    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final Set<Integer> RETRYABLE_CODES = Set.of(
        BAD_GATEWAY,
        SERVICE_UNAVAILABLE,
        GATEWAY_TIMEOUT
    );
}