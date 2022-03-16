package com.example.demo.JWT;

import com.example.demo.dto.UserInfo;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userServiceImpl;

    public UserDetails loadUserByUsername(String userId,String userPw) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = Optional.ofNullable(userServiceImpl.selectUserByIdAndPw(userId, userPw));
        if (!userInfo.isEmpty()){
            List<String> roles = new ArrayList<>();
            if(userInfo.get().getUserId().equals("master")){
                roles.add("ROLE_ADMIN");
            }else{
                roles.add("ROLE_USER");
            }
            UserDetails userDetails = new CustomUserDetails(userInfo.get().getUserId(),userInfo.get().getUserPw(),roles);
            return userDetails;
        }else{
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
