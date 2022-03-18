package com.example.demo.controller;

import com.example.demo.dto.UserServiceValue;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceValueService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service")
public class UserServiceValueController {

    private final UserServiceValueService userServiceValueServiceImpl;

    /**
     * 설명 : 잔여연차조회 API, id로 잔여연차 조회
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "잔여연차 조회", response = String.class)
    @GetMapping("/leave")
    public ResponseEntity<String> getAnnualLeave(@RequestParam String id){
        String returnResult = userServiceValueServiceImpl.selectLeaveById(id);
        if(returnResult != null){
            return new ResponseEntity<String>(returnResult, HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("연차조회에 실패했습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 설명 : 잔여연차입력 API, id와 잔여연차로 입력
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용안함
     */
//    @ApiOperation(value = "잔여연차 입력", response = String.class)
//    @PostMapping("/leave")
//    public ResponseEntity<String> insertAnnualLeave(@RequestBody UserServiceValue userServiceValue){
//        int returnResult = userServiceValueServiceImpl.insertLeaveById(userServiceValue);
//        if(returnResult >= 1){
//            return new ResponseEntity<String>("연차 입력 완료",HttpStatus.OK);
//        }else {
//            return new ResponseEntity<String>("연차조회에 실패했습니다.", HttpStatus.NOT_FOUND);
//        }
//    }

    /**
     * 설명 : 잔여연차수정 API, id와 잔여연차로 수정가능
     * 수정일자 : 220316
     * 변경사항 : -
     * 현재상태 : 사용중
     */
    @ApiOperation(value = "잔여연차 수정", response = String.class)
    @PutMapping("/leave")
    public ResponseEntity<String> updateAnnualLeave(@RequestBody UserServiceValue userServiceValue){
        String returnMsg = "";
        if (userServiceValue.getUserId() == null || userServiceValue.getAnnualLeave() == null){
            returnMsg = "아이디나 잔여연차가 입력되지 않았습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.BAD_REQUEST);
        }
        try{
            int returnResult = userServiceValueServiceImpl.updateLeaveById(userServiceValue.getAnnualLeave(), userServiceValue.getUserId());
            if(returnResult >= 1){
                returnMsg = "수정 완료했습니다.";
            }else {
                returnMsg = "연차수정에 실패했습니다.";
                return new ResponseEntity<String>(returnMsg, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            log.warn("/leave 에러 발생 {}", e.getMessage());
            returnMsg = "알수없는 에러가 발생하였습니다.";
            return new ResponseEntity<String>(returnMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(returnMsg, HttpStatus.OK);
    }
}
