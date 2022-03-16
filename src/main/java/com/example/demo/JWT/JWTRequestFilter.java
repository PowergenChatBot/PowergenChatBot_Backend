package com.example.demo.JWT;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.UserInfo;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        String path = request.getRequestURI();
        System.out.println(path);
        // 아래의 경로에는 이 필터가 적용되지 않는다.
        // /auth 이외의 부분에서는 전부 필터 적용 예정
//        if (path.startsWith("/auth") || path.startsWith("/swagger") || path.startsWith("/webjar")) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        try{
            final String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null){
                Claims claims = jwtUtil.parseJwtToken(authorizationHeader);
                System.out.println(claims);
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
            log.trace("필터에서 에러 발생",e);
        }

        filterChain.doFilter(request, response);
    }

}
