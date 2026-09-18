package com.ifast.ipaymy.oauth2.config.security.handler;

import com.ifast.ipaymy.backend.util.exception.writer.ErrorResponseWriters;
import com.ifast.ipaymy.oauth2.config.security.enums.OAuth2ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AccessDefinedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
        throws IOException {
        ErrorResponseWriters.create(request, response)
            .write(HttpStatus.FORBIDDEN, OAuth2ErrorCodeEnum.FORBIDDEN);
    }
}
