package com.ifast.ipaymy.oauth2.config.security.handler;

import com.ifast.ipaymy.backend.util.exception.writer.ErrorResponseWriters;
import com.ifast.ipaymy.oauth2.config.security.enums.OAuth2ErrorCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
        throws IOException {
        ErrorResponseWriters.create(request, response)
            .write(HttpStatus.UNAUTHORIZED, OAuth2ErrorCodeEnum.UNAUTHORIZED);
    }
}
