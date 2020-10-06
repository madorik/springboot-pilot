package com.estsoft.pilot.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof AuthenticationServiceException) {
            request.setAttribute("loginFailMsg", "존재하지 않는 사용자입니다.");
        } else if (exception instanceof BadCredentialsException) {
            request.setAttribute("loginFailMsg", "로그인 정보가 틀립니다.");
        } else {
            request.setAttribute("loginFailMsg", "로그인에 실패하였습니다.");
        }
        log.error(exception.getMessage());
        request.getRequestDispatcher("/login/fail").forward(request, response);
    }
}
