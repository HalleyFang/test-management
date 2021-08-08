package com.testmanage.mapper;

import com.testmanage.entity.MyUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    void addUser(MyUser user);
    void updateUser(MyUser user);
    void deleteUser(MyUser user);
    MyUser getUserByName(String username);
    List<MyUser> getAllUser();
    List<MyUser> getAllUserIgnore(String username);
}
