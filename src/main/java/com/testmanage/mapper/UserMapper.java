package com.testmanage.mapper;

import com.testmanage.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    User addUser(User user);
    User updateUser(User user);
    User deleteUser(User user);
    User getUserByName(String name);
    List<User> getAllUser();
}
