package com.das.config;

import com.das.responses.ApiErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
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

        ApiErrorResponse res = new ApiErrorResponse(message);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.print(new Gson().toJson(res));
        out.flush();
    }
}