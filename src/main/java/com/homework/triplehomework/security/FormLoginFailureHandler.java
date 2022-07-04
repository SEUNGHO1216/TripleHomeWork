package com.homework.triplehomework.security;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FormLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errormessage = "아이디와 비밀번호를 확인해주세요.";
        if (exception instanceof DisabledException) {
            errormessage = "휴면계정입니다.";
        } else if (exception instanceof AuthenticationServiceException) {
            errormessage = "존재하지 않는 계정입니다.";
        } else if (exception instanceof BadCredentialsException) {
            errormessage = "비밀번호가 일치하지 않습니다.";
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        JSONObject json = new JSONObject();
        json.put("exception",errormessage);

        out.print(json);
    }
}