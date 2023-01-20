package com.sns.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


//2~4 jwt생성 시 필요한 정보이며 해당 정보는 리소스파일에서 로드함

@Component
public class JwtTokenizer {
    @Getter
    @Value("${jwt.key}")
    private String secretKey;  //yml의 SECRET KEY 정보/ 생성 및 검증될 떄 사용됨

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;  //Access 만료 시간 정보

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;  //Refresh 만료 시간 정보

    //  Plain Text 형태인 Key 의 byte[]를 base64 형식 문자열로 인코딩
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //Access Token 생성 메서드
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);  //디코딩 후 HMAC 알고리즘을 적용한 Key 객체

        return Jwts.builder()
                .setClaims(claims)  //인증된 사용자와 관련된 정보 추가
                .setSubject(subject)  //JWT 제목 추가
                .setIssuedAt(Calendar.getInstance().getTime())  //Token 발행 일자 설정
                .setExpiration(expiration)  //만료 일시 지정
                .signWith(key)  //서명을 위한 객체 설정
                .compact();  //생성 및 직렬화(byte 형태로 데이터를 변환)
    }

    // Access 토큰 만료 시 새로 생성할 수 있게 해주는 메서드(Refresh Token)
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        //새로운 토큰 발급으로 Claims 제외
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    // Signature 검증 후 Claims 반환 용도
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
        return claims;
    }

    // 단순히 검증만 하는 용도로 쓰일 경우
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    //만료일자 구하기
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }
    //서명에 사용할 Secret Key 생성
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);  //base64 형식으로 인코딩된 Secret Key를 디코딩한것을 Key byte array로 선언
        Key key = Keys.hmacShaKeyFor(keyBytes);  //key byte array에 HMAC 알고리즘을 적용한  Key 객체 생성

        return key;
    }
}
