package com.example.demo.controller;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.dto.UserInfo;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImpl;
import com.example.demo.util.PWHashingUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userServiceImpl;

    private final PWHashingUtil pwHashingUtil;

    private final JWTUtil jwtUtil;

    /**
     * 설명 : 잔여연차조회 API, id로 잔여연차 조회
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "로그인할때 사용, 토큰 반환", response = UserInfo.class)
    @GetMapping("/login")
    public ResponseEntity<String> getUserLogin(@RequestParam String id, @RequestParam String pw){
        try {
            String hashPW = pwHashingUtil.Hashing(pw.getBytes(),id);
            Optional<UserInfo> userInfo = Optional.ofNullable(userServiceImpl.selectUserByIdAndPw(id, hashPW));
            if(!userInfo.isEmpty()){
                String resultToken = jwtUtil.makeJwtToken(userInfo.get().getUserId(),userInfo.get().getUserPw());
                return new ResponseEntity<String>(resultToken, HttpStatus.OK);
            }else {
                return new ResponseEntity<String>("로그인 실패", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>("알수 없는 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
