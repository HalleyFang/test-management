package com.testmanage.service;

import com.testmanage.entity.User;
import com.testmanage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User getUserByName(String name){
        return userMapper.getUserByName(name);
    }
}
