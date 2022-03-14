package com.example.demo.dao;

import com.example.demo.dto.UserServiceValue;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserServiceDAO {
    String selectLeaveById(String id);

//    int insertLeaveById(UserServiceValue userServiceValue);

    int updateLeaveById(String leave, String id);
}
