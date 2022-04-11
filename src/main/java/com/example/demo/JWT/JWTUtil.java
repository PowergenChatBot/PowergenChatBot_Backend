package com.example.demo.JWT;

import com.example.demo.dto.UserInfo;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Component
public class JWTUtil {

    @Value("${jwt.encryptor.password}")
    private String password;

    public String makeJwtToken(String userId, String userPw) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더의 타입을 지정 jwt이기 때문에 jwt_type으로 사용
                .setIssuer("powergen") // 토큰 발급자 설정
                .setIssuedAt(now) // 발급 날짜
                .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis())) // 만료 시간 지정 : 발급일로 부터 7일 이후 만료
//                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(1).toMillis()))
                .claim("userId", userId)
                .claim("userPw", userPw)
                .signWith(SignatureAlgorithm.HS256,password)
                .compact();
    }

    /**
     * 저장한 유저 정보 가져오기
     * @param authorizationHeader
     * @return
     */
    public Claims parseJwtToken(String authorizationHeader) {
        if(authorizationHeader == null){
            return null;
        }
        String token = extractToken(authorizationHeader);

        if(!validationAuthorizationHeader(token)){
            return null;
        }

        return Jwts.parser()
                .setSigningKey(password)
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * 유효성 검사
     * @param token
     */
    private boolean validationAuthorizationHeader(String token) {
        try {
            Jwts.parser().setSigningKey(password).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * Bearer 뗀 값으로 변경
     * @param authorizationHeader
     * @return
     */
    private String extractToken(String authorizationHeader){
        return authorizationHeader.substring("Bearer ".length());
    }
}
