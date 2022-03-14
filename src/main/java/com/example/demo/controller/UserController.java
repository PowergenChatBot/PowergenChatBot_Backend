package com.example.demo.controller;

import com.example.demo.common.JWTConfig;
import com.example.demo.common.PWHashingConfig;
import com.example.demo.dto.UserInfo;
import com.example.demo.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    PWHashingConfig pwHashingConfig = new PWHashingConfig();

    @ApiOperation(value = "회원정보 리스트 가져오기", response = List.class)
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        JWTConfig jwtConfig = new JWTConfig();
        try{
//            String newPW = pwHashingConfig.Hashing("test".getBytes(),"test");
//            System.out.println(newPW);
        }catch (Exception e) {
            System.out.println("에러났당");
        }
        return new ResponseEntity<String>("한글이안돼요", HttpStatus.OK);
    }

    @ApiOperation(value = "회원정보 리스트 가져오기", response = List.class)
    @GetMapping
    public ResponseEntity<List<UserInfo>> getListUserInfo(){
        return new ResponseEntity<List<UserInfo>>(userServiceImpl.selectUser(), HttpStatus.OK);
    }

    @ApiOperation(value = "로그인할때 사용", response = UserInfo.class)
    @GetMapping("/login")
    public ResponseEntity<Optional<UserInfo>> getUserLogin(@RequestParam String id, @RequestParam String pw){
        Optional<UserInfo> userInfo = Optional.ofNullable(userServiceImpl.selectUserByIdAndPw(id, pw));
        if(!userInfo.isEmpty()){
            return new ResponseEntity<Optional<UserInfo>>(userInfo, HttpStatus.OK);
        }else {
            return new ResponseEntity<Optional<UserInfo>>(userInfo, HttpStatus.NOT_FOUND);
        }
    }

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

    @ApiOperation(value = "회원가입", response = String.class)
    @PostMapping("/signUp")
    public ResponseEntity<String> insertUserSignUp(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        try{
            int resultRow = userServiceImpl.insertUser(userInfo);
            int serviceResultRow = userServiceImpl.insertUserServiceValue(userInfo.getUserId());
            returnMsg = "회원가입에 성공하였습니다.";
        }catch (DuplicateKeyException e){
            returnMsg = "중복된 id 값을 입력하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 변경, JSON안에 userId와 userPw만 넣어도 진행", response = String.class)
    @PutMapping("/updatePw")
    public ResponseEntity<String> updatePassword(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        try{
            int resultRow = userServiceImpl.updatePassword(userInfo);
            if(resultRow >= 1){
                returnMsg = "비밀번호 변경에 성공하였습니다.";
            }else{
                returnMsg = "비밀번호 변경 실패";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    @ApiOperation(value = "현재 비밀번호 확인, JSON안에 id pw 만 넣으면 확인", response = String.class)
    @PostMapping("/checkCurrentPw")
    public ResponseEntity<String> checkCurrentPw(@RequestBody UserInfo userInfo){
        String returnMsg = "";
        try{
            String resultValue = userServiceImpl.selectCurrentPasswordByUserId(userInfo);
            if(resultValue.equals(userInfo.getUserPw())){
                returnMsg = "현재 비밀번호와 일치합니다.";
            }else{
                returnMsg = "현재 비밀번호와 일치하지 않습니다";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    @ApiOperation(value = "재직여부 No로 변경, 인자값 id", response = String.class)
    @GetMapping("/employed")
    public ResponseEntity<String> updateCurrentlyEmployed(@RequestParam String id){
        String returnMsg = "";
        try{
            int resultRow = userServiceImpl.updateCurrentlyEmployed(id);
            if(resultRow >= 1){
                returnMsg = "재직여부 변경에 성공했습니다.";
            }else{
                returnMsg = "재직여부 변경 실패, 아이디를 확인해주세요.";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

    @ApiOperation(value = "직급 변경, 직급이랑 아이디 넘겨주세요 param으로", response = String.class)
    @PutMapping("/updateRank")
    public ResponseEntity<String> updateRank(@RequestParam String rank, @RequestParam String id){
        String returnMsg = "";
        try{
            int resultRow = userServiceImpl.updateRankById(rank, id);
            if(resultRow >= 1){
                returnMsg = "직급 변경에 성공하였습니다.";
            }else{
                returnMsg = "직급 변경 실패, 아이디를 확인해주세요";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }

}
