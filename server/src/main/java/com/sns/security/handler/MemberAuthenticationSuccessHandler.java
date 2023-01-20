package com.sns.security.handler;

import com.google.gson.Gson;
import com.sns.security.dto.LoginDto;
import com.sns.security.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//로그인인증에 성공 시 로그기록, 로그인에 성공한 사용자의 정보를 resposne로 전ㅇ송하는 추가 처리핸들러/실패했을때의 추가처리 가능
@Slf4j
@Component
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("# Authentication successfully");
        sendSuccessResponse(response);

    }

    private void sendSuccessResponse(HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(gson.toJson(new LoginResponseDto("log-in success")));

    }
}
