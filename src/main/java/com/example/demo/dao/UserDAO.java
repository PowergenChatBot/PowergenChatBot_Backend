package com.example.demo.dao;

import com.example.demo.dto.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDAO {

    List<UserInfo> selectUser();
    UserInfo selectUserByIdAndPw(String id, String pw);
    List<Map<String,String>> selectPhoneByName(String name);
    int updatePassword(UserInfo userInfo);
    int insertUser(UserInfo userInfo);
    int insertUserServiceValue(String id);
    int updateCurrentlyEmployed(UserInfo userInfo);
    String selectCurrentPasswordByUserId(UserInfo userInfo);
    int updateRankById(String rank, String id);
}
