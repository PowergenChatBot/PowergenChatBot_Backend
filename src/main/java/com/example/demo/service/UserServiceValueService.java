package com.example.demo.service;

import com.example.demo.dto.UserServiceValue;

public interface UserServiceValueService {

    String selectLeaveById(String id);

//    int insertLeaveById(UserServiceValue userServiceValue);

    int updateLeaveById(String leave, String id);

}
