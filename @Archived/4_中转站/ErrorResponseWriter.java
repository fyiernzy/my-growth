package com.ifast.ipaymy.backend.util.exception.writer;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;


public interface ErrorResponseWriter {

    void write(HttpStatus httpStatus, ErrorCode errorCode) throws IOException;

    void write(HttpStatus httpStatus, ErrorCode errorCode, Map<String, Object> context)
        throws IOException;

}
