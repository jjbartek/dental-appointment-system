package com.das.common.exceptions;

import com.das.common.responses.errors.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UnauthenticatedRequestHandler implements AuthenticationEntryPoint {
    private final MessageSource messageSource;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String message = messageSource.getMessage(authException.getMessage(), null, null, Locale.getDefault());

        if (message == null) message = authException.getMessage();

        ErrorResponse res = new ErrorResponse();
        res.setCode(401);
        if (authException instanceof BadCredentialsException ||
                authException instanceof CredentialsExpiredException ||
                authException instanceof UsernameNotFoundException) {
            res.setMessage(message);
        } else {
            res.setMessage("Unauthorized");
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.print(new Gson().toJson(res));
        out.flush();
    }
}