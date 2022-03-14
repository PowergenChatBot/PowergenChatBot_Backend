package com.example.demo.controller;

import com.example.demo.dto.UserServiceValue;
import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceValueService;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/service")
public class UserServiceValueController {

    @Autowired
    private UserServiceValueService userServiceValueServiceImpl;

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

    @ApiOperation(value = "잔여연차 수정", response = String.class)
    @PutMapping("/leave")
    public ResponseEntity<String> updateAnnualLeave(@RequestParam String leave, @RequestParam String id){
        int returnResult = userServiceValueServiceImpl.updateLeaveById(leave, id);
        if(returnResult >= 1){
            return new ResponseEntity<String>("수정 완료했습니다.", HttpStatus.OK);
        }else {
            return new ResponseEntity<String>("연차조회에 실패했습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
