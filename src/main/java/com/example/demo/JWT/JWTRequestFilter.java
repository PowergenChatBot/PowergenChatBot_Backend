package com.example.demo.JWT;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.UserInfo;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Component
@RequiredArgsConstructor
public class JWTRequestFilter extends OncePerRequestFilter {

    private final UserService userServiceImpl;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("What");
        String path = request.getRequestURI();
        System.out.println(path);
        // 아래의 경로에는 이 필터가 적용되지 않는다.
        // /auth 이외의 부분에서는 전부 필터 적용 예정
        if (path.startsWith("/auth") || path.startsWith("/swagger") || path.startsWith("/webjar")) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null){
                Claims claims = jwtUtil.parseJwtToken(authorizationHeader);
                System.out.println(claims);
                if(claims == null){
                    return;
                }

                String userId = claims.get("userId",String.class);
                String userPw = claims.get("userPw",String.class);

                Optional<UserInfo> userInfo = Optional.ofNullable(userServiceImpl.selectUserByIdAndPw(userId, userPw));

                if(!userInfo.isEmpty()) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(userId,userPw)
                    );
                }
            }else {
                filterChain.doFilter(request, response);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        filterChain.doFilter(request, response);
    }

}
