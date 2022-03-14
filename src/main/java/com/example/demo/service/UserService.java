package com.example.demo.service;

import com.example.demo.dto.UserInfo;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

public interface UserService{

    List<UserInfo> selectUser();

    UserInfo selectUserByIdAndPw(String id, String pw);

    List<Map<String,String>> selectPhoneByName(String name);

    int updatePassword(UserInfo userInfo);

    int insertUser(UserInfo userInfo);

    int insertUserServiceValue(String id);

    int updateCurrentlyEmployed(String id);

    String selectCurrentPasswordByUserId(UserInfo userInfo);

    int updateRankById(String rank, String id);
}
