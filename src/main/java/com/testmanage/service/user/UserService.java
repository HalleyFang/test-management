package com.testmanage.service.user;

import com.testmanage.entity.MyUser;
import com.testmanage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public MyUser getUserByName(String name) {
        MyUser user = userMapper.getUserByName(name);
        return user;
    }

    @Cacheable("usersIgnoreAdmin")
    public List<MyUser> getUsersIgnoreAdmin(){
        return userMapper.getAllUserIgnore("admin");
    }

    @Cacheable("users")
    public List<MyUser> getUsers(){
        return userMapper.getAllUser();
    }

}
