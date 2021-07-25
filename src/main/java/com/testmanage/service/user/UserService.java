package com.testmanage.service.user;

import com.testmanage.entity.MyUser;
import com.testmanage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public MyUser getUserByName(String name) {
        MyUser user = userMapper.getUserByName(name);
        return user;
    }
}
