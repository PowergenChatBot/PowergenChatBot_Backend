package com.example.demo.JWT;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try{
            final String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null){
                Claims claims = jwtUtil.parseJwtToken(authorizationHeader);
                if(claims == null){
                    throw new Exception("토큰이 잘못 되었습니다.");
                }

                String userId = claims.get("userId",String.class);
                String userPw = claims.get("userPw",String.class);

                UserDetails userDetails = customUserDetailService.loadUserByUsername(userId,userPw);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }catch (Exception e){
            log.warn("필터에서 에러 발생 {} ", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
