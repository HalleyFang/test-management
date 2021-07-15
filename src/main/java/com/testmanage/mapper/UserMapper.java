package com.testmanage.mapper;

import com.testmanage.entity.MyUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    MyUser addUser(MyUser user);
    MyUser updateUser(MyUser user);
    MyUser deleteUser(MyUser user);
    MyUser getUserByName(String username);
    List<MyUser> getAllUser();
}
