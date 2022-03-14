package com.example.demo.service;

import com.example.demo.dao.UserServiceDAO;
import com.example.demo.dto.UserServiceValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceValueServiceImpl implements UserServiceValueService{

    @Autowired
    UserServiceDAO userServiceDAO;

    @Override
    public String selectLeaveById(String id) {
        return userServiceDAO.selectLeaveById(id);
    }

//    @Override
//    public int insertLeaveById(UserServiceValue userServiceValue) {
//        return userServiceDAO.insertLeaveById(userServiceValue);
//    }

    @Override
    public int updateLeaveById(String leave, String id) {
        return userServiceDAO.updateLeaveById(leave, id);
    }

}
