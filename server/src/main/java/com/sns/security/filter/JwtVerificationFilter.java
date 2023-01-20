package com.sns.security.filter;

import com.sns.common.exception.ExceptionCode;
import com.sns.security.utils.CustomAuthorityUtils;
import com.sns.security.jwt.JwtTokenizer;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {  //한번의 JWT 검증 요청을 하는 filter 상속
    private final JwtTokenizer jwtTokenizer;  //JWT 검증 및 토큰에 포함된 정보만 Claims를 얻는데 사용함
    private final CustomAuthorityUtils authorityUtils;  //검증 성공 후 Authentication 갹체에 채울 사용자의 권한을 생성하는데 사용



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {    //예외처리 로직
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);  //Security Context에 Authentication을 저장
        } catch (SignatureException signatureException) {
            request.setAttribute("exception", ExceptionCode.INVALID_TOKEN);
        } catch (ExpiredJwtException expiredJwtException) {
            request.setAttribute("exception", ExceptionCode.EXPIRED_TOKEN);
        } catch (Exception exception) {
            request.setAttribute("exception",ExceptionCode.UNAUTHORIZED);
        }
        filterChain.doFilter(request,response);  //다음 Filter 호출
    }

    //true일경우 실행되지않고 다음 Filter 로 넘어감
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");  //요청 Header 의 Authorization 값을 가져옴
        return authorization == null || !authorization.startsWith("Bearer");  //Header 값이 null or "Bearer" 로 시작하지 않으면 다음 Filter 이어서 실행
   }
    //JWT 검증 메서드
    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws =request.getHeader("Authorization").replace("Bearer" , "");  //request header에서 JWT를 추출한 뒤, JWT Header(type: "Bearer) 제거
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());  //검증하기 위한 인코딩된 Secret Key
        Map<String, Object> claims = jwtTokenizer.getClaims(jws,base64EncodedSecretKey).getBody();  //JWT claims 부분 파싱
        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List)claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
