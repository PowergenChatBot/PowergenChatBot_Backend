package com.example.demo.common;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;

public class JWTConfig {

    public String makeJwtToken(String id, String pw) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더의 타입을 지정 jwt이기 때문에 jwt_type으로 사용
                .setIssuer("powergen") // 토큰 발급자 설정
                .setIssuedAt(now) // 발급 날짜
                .setExpiration(new Date(now.getTime() + Duration.ofDays(7).toMillis())) // 만료 시간 지정 : 발급일로 부터 7일 이후 만료
                .claim("id", id)
                .claim("pw", pw)
                .signWith(SignatureAlgorithm.HS256,"powergen")
                .compact();
    }
}
