package com.ifast.ipaymy.oauth2.config.security.enums;

import com.ifast.ipaymy.backend.util.exception.writer.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuth2ErrorCodeEnum implements ErrorCode {
    FORBIDDEN("OAUTH2_001", "forbidden.api"),
    UNAUTHORIZED("OAUTH2_002", "unauthorized.api");

    private final String errorCode;
    private final String errorMessage;

    @Override
    public String errorCode() {
        return errorCode;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
