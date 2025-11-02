package com.ifast.ipaymy.backend.util.exception.writer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorResponseWriters {

    public static ErrorResponseWriter create(HttpServletRequest httpServletRequest,
                                             HttpServletResponse httpServletResponse) {
        return HttpServletErrorResponseWriter.of(httpServletRequest, httpServletResponse);
    }
}
