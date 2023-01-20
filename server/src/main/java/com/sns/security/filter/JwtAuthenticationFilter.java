package com.sns.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.member.domain.entity.Member;
import com.sns.security.dto.LoginDto;
import com.sns.security.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
// UsernamePasswordAuthenticationFilter: 플러그인 방식에서 사용되는 디폴트 Security Filter -> Username/Password 기반의
// 인증을 처리하기 위해 확장구현
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    //메서드 내부 인증
    @SneakyThrows  //명시적인 예외 처리를 생략(throws 혹은 try-catch 구문 생략 -> request.getInputStream()의 throw 구문 생략을 위함
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();  // 역직렬화를 위한 오브젝트매퍼 인스턴스 생성(JSON 형식을 사용할 때 직렬화, 역직렬화 할 때 ObjectMapper 를 사용)
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(),LoginDto.class);  //ServletInputStream을 loginDto 객체로 역직렬화

        //Username 과 Password 정보를 포함한 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);  //AuthenticationManager 에게 UsernamePasswordAuthenticationToken 전달
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        Member member = (Member) authResult.getPrincipal();  //인증

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("Refresh",  refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);

    }

    private String delegateAccessToken(Member member) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims,subject,expiration,base64EncodedSecretKey);
        return accessToken;
    }

    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String refreshToken = jwtTokenizer.generateRefreshToken(subject,expiration,base64EncodedSecretKey);
        return refreshToken;
    }

}


