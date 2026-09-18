package com.ifast.ipaymy.backend.util.exception.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifast.ipaymy.backend.util.exception.ErrorResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor(staticName = "of")
public class HttpServletErrorResponseWriter implements ErrorResponseWriter {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    public void write(HttpStatus httpStatus, ErrorCode errorCode)
        throws IOException {
        write(httpStatus, errorCode, null);
    }

    public void write(HttpStatus httpStatus,
                      ErrorCode errorCode,
                      Map<String, Object> context)
        throws IOException {
        ErrorResponseModel errorResponseModel = new ErrorResponseModel(
            errorCode.errorCode(),
            errorCode.errorMessage(),
            httpServletRequest.getRequestURI(),
            context
        );
        httpServletResponse.setStatus(httpStatus.value());
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpServletResponse.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), errorResponseModel);
    }
}
