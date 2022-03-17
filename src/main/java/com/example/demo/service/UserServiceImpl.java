package com.example.demo.service;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<UserInfo> selectUser() {
        return userDAO.selectUser();
    }

    @Override
    public UserInfo selectUserByIdAndPw(String id, String pw) {
        return userDAO.selectUserByIdAndPw(id, pw);
    }

    @Override
    public List<Map<String, String>> selectPhoneByName(String name) {
        return userDAO.selectPhoneByName(name);
    }

    @Override
    public int updatePassword(UserInfo userInfo) {
        return userDAO.updatePassword(userInfo);
    }

    @Override
    public int insertUser(UserInfo userInfo) {
        return userDAO.insertUser(userInfo);
    }

    @Override
    public int insertUserServiceValue(String id) {
        return userDAO.insertUserServiceValue(id);
    }

    @Override
    public int updateCurrentlyEmployed(UserInfo userInfo) {
        return userDAO.updateCurrentlyEmployed(userInfo);
    }

    @Override
    public String selectCurrentPasswordByUserId(UserInfo userInfo) {
        return userDAO.selectCurrentPasswordByUserId(userInfo);
    }

    @Override
    public int updateRankById(String rank, String id) {
        return userDAO.updateRankById(rank, id);
    }

}
