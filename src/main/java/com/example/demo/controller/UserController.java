package com.example.demo.controller;

import com.example.demo.JWT.JWTUtil;
import com.example.demo.util.PWHashingUtil;
import com.example.demo.dto.UserInfo;
import com.example.demo.service.UserService;
import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userServiceImpl;

    private final PWHashingUtil pwHashingUtil;

    private final JWTUtil jwtUtil;

    /**
     * 설명 : api 테스트용
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 테스트시 사용
     */
//    @ApiOperation(value = "테스트용", response = List.class)
//    @GetMapping("/test")
//    public ResponseEntity<String> test(){
//        try{
//            String newPW = pwHashingUtil.Hashing("test".getBytes(),"test");
//            System.out.println(newPW);
//        }catch (Exception e) {
//            System.out.println("에러났당");
//        }
//        return new ResponseEntity<String>("한글이안돼요", HttpStatus.OK);
//    }

    /**
     * 설명 : 회원정보 리스트 전부를 가져오는 기능
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용안함
     */
//    @ApiOperation(value = "회원정보 리스트 가져오기", response = List.class)
//    @GetMapping
//    public ResponseEntity<List<UserInfo>> getListUserInfo(){
//        return new ResponseEntity<List<UserInfo>>(userServiceImpl.selectUser(), HttpStatus.OK);
//    }

    /**
     * 설명 : 로그인할 때 id, pw 받아서 jwtToken 을 반환해준다.
     * 수정일자 : 220316
     * 변경사항 : Security 사용으로 인한 AuthController로 변경
     * 현재상태 : 사용안함
     */
//    @ApiOperation(value = "로그인할때 사용", response = UserInfo.class)
//    @GetMapping("/login")
//    public ResponseEntity<String> getUserLogin(@RequestParam String id, @RequestParam String pw){
//        try {
//            String hashPW = pwHashingUtil.Hashing(pw.getBytes(),id);
//            Optional<UserInfo> userInfo = Optional.ofNullable(userServiceImpl.selectUserByIdAndPw(id, hashPW));
//            if(!userInfo.isEmpty()){
//                String resultToken = jwtUtil.makeJwtToken(userInfo.get().getUserId(),userInfo.get().getUserPw());
//                return new ResponseEntity<String>(resultToken, HttpStatus.OK);
//            }else {
//                return new ResponseEntity<String>("로그인 실패", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<String>("알수 없는 에러가 발생했습니다.", HttpStatus.NOT_FOUND);
//        }
//    }
    /**
     * 설명 : RequestParam 으로 이름을 받아서 그 이름에 해당하는 유저정보를 가져다주는 API
     * 수정일자 : 220526
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "이름으로 유저정보조회", response = List.class)
    @GetMapping("/userByName")
    public ResponseEntity<Object> getUserInfoByPhone(@RequestParam String name) {
        Optional<UserInfo> returnResult = Optional.ofNullable(userServiceImpl.selectUserInfoByName(name));
        if(!returnResult.isEmpty()){
            return new ResponseEntity<>(returnResult, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 설명 : RequestParam 으로 이름을 받아서 그 이름에 해당하는 인원의 전화번호를 제공 해주는 API
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "전화번호 조회", response = List.class)
    @GetMapping("/phone")
    public ResponseEntity<Object> getPhoneNumber(@RequestParam String name) {
        name = name + "%";
        List<Map<String, String>> returnResult = userServiceImpl.selectPhoneByName(name);
        if(!returnResult.isEmpty()){
            return new ResponseEntity<>(returnResult, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 설명 : 회원가입을 해주는 api / body 에 userInfo 받고 테이블에 정보 삽입
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "회원가입", response = String.class)
    @PostMapping("/signUp")
    public ResponseEntity<String> insertUserSignUp(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        try{
            userInfo.setUserPw(pwHashingUtil.Hashing(userInfo.getUserPw().getBytes(),userInfo.getUserId()));
            int resultRow = userServiceImpl.insertUser(userInfo);
            int serviceResultRow = userServiceImpl.insertUserServiceValue(userInfo.getUserId());
            returnMsg = "회원가입에 성공하였습니다.";
        }catch (DuplicateKeyException e){
            returnMsg = "중복된 id 값을 입력하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.warn("/Signup 에러 발생 {}", e.getMessage());
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    /**
     * 설명 : 비밀번호 변경 API, body 에 userid와 userpw만 집어넣도 작동
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "비밀번호 변경", response = String.class)
    @PutMapping("/updatePw")
    public ResponseEntity<String> updatePassword(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        if (userInfo.getUserId() == null || userInfo.getUserPw() == null){
            returnMsg = "아이디나 비밀번호가 입력되지 않았습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.BAD_REQUEST);
        }
        try{
            userInfo.setUserPw(pwHashingUtil.Hashing(userInfo.getUserPw().getBytes(),userInfo.getUserId()));
            int resultRow = userServiceImpl.updatePassword(userInfo);
            if(resultRow >= 1){
                returnMsg = "비밀번호 변경에 성공하였습니다.";
            }else{
                returnMsg = "비밀번호 변경 실패";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.warn("/updatePw 에러 발생 {}", e.getMessage());
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    /**
     * 설명 : 비밀번호 변경 API, body 에 userid와 userpw만 집어넣도 작동
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "현재 비밀번호 확인", response = String.class)
    @PostMapping("/checkCurrentPw")
    public ResponseEntity<String> checkCurrentPw(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        if (userInfo.getUserId() == null || userInfo.getUserPw() == null){
            returnMsg = "아이디나 비밀번호가 입력되지 않았습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.BAD_REQUEST);
        }
        try{
            String resultValue = userServiceImpl.selectCurrentPasswordByUserId(userInfo);
            if(pwHashingUtil.Hashing(userInfo.getUserPw().getBytes(),userInfo.getUserId()).equals(resultValue)){
                returnMsg = "현재 비밀번호와 일치합니다.";
            }else{
                returnMsg = "현재 비밀번호와 일치하지 않습니다";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.warn("/checkCurrentPw 에러 발생 {}", e.getMessage());
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    /**
     * 설명 : 재직여부 변경 API, body 에 userid 와 currently_employed 필요
     * 수정일자 : 220316
     * 변경사항 : No Yes 선택할수있게 변경
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "재직여부 변경", response = String.class)
    @PutMapping("/updateCurEmploy")
    public ResponseEntity<String> updateCurrentlyEmployed(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        if (userInfo.getUserId() == null || userInfo.getCurrentlyEmployed() == null){
            returnMsg = "아이디나 재직여부가 입력되지 않았습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.BAD_REQUEST);
        }
        try{
            int resultRow = userServiceImpl.updateCurrentlyEmployed(userInfo);
            if(resultRow >= 1){
                returnMsg = "재직여부 변경에 성공했습니다.";
            }else{
                returnMsg = "재직여부 변경 실패, 아이디를 확인해주세요.";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.warn("/updateCurEmploy 에러 발생 {}", e.getMessage());
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    /**
     * 설명 : 직급 변경 API, body 에 userid와 rank만 집어넣도 작동
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "직급 변경", response = String.class)
    @PutMapping("/updateRank")
    public ResponseEntity<String> updateRank(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        if (userInfo.getUserId() == null || userInfo.getRank() == null){
            returnMsg = "아이디나 직급이 입력되지 않았습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.BAD_REQUEST);
        }
        try{
            int resultRow = userServiceImpl.updateRankById(userInfo.getRank(), userInfo.getUserId());
            if(resultRow >= 1){
                returnMsg = "직급 변경에 성공하였습니다.";
            }else{
                returnMsg = "직급 변경 실패, 아이디를 확인해주세요";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.warn("/updateRank 에러 발생 {}", e.getMessage());
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    /**
     * 설명 : 토큰입력받아서 유저 정보리턴하는 API
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "토큰을 입력받아 유저 정보를 반환", response = UserInfo.class)
    @GetMapping
    public ResponseEntity<UserInfo> getUserInfoByToken(HttpServletRequest httpServletRequest){
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if(authorizationHeader == null){
            return new ResponseEntity<UserInfo>(HttpStatus.BAD_REQUEST);
        }
        Claims claims = jwtUtil.parseJwtToken(authorizationHeader);
        return new ResponseEntity<UserInfo>(userServiceImpl.selectUserByIdAndPw(
                claims.get("userId",String.class),claims.get("userPw",String.class)), HttpStatus.OK);
    }

}
