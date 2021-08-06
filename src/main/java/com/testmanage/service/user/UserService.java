package com.testmanage.service.user;

import com.testmanage.entity.MyUser;
import com.testmanage.entity.Permissions;
import com.testmanage.entity.Roles;
import com.testmanage.entity.Sequence;
import com.testmanage.mapper.UserMapper;
import com.testmanage.utils.RandomString;
import com.testmanage.utils.SequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    SequenceUtil sequenceUtil;

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

    @CacheEvict("users")
    public void addUser(MyUser user) throws Exception {
        MyUser u = getUserByName(user.getUsername());
        if(u instanceof MyUser){
            throw new Exception("用户名已存在");
        }
        Integer id = sequenceUtil.getNext("userId");
        user.setId(id);
        Md5Hash md5Hash = new Md5Hash(user.getPassword());
        String salt = RandomString.randomAlphabetic(8);
        md5Hash.setSalt(ByteSource.Util.bytes(salt));
        md5Hash.setIterations(1024);
        user.setPassword(md5Hash.toHex());
        user.setSalt(salt);
        userMapper.addUser(user);
    }

    @CacheEvict("users")
    public void updateUser(MyUser user,String type) throws Exception {
        MyUser u = getUserByName(user.getUsername());
        if(u instanceof MyUser) {
            if(!StringUtils.isEmpty(type) && type.equalsIgnoreCase("changePassword")) {
                Md5Hash md5Hash = new Md5Hash(user.getPassword());
                String salt = RandomString.randomAlphabetic(8);
                md5Hash.setSalt(ByteSource.Util.bytes(salt));
                md5Hash.setIterations(1024);
                user.setPassword(md5Hash.toHex());
                user.setSalt(salt);
            }else {
                user = diffUser(u,user);
                if (user.getRoles() == null && user.getPermissions() == null){
                    return;
                }
            }
            userMapper.updateUser(user);
        }else {
            throw new Exception("用户不存在");
        }
    }

    /**
     *
     * @param ub 当前对象
     * @param ut 待更新对象
     * @return
     */
    private MyUser diffUser(MyUser ub,MyUser ut){
        if(ub!=null && ut.getRoles() != null ){
            Boolean isDiff = false;
            if(ub.getRoles().size()==ut.getRoles().size()){
                for(Roles roles:ub.getRoles()){
                    if(!ub.getRoles().contains(roles)){
                        isDiff = true;
                        break;
                    }
                }
            }
            if(!isDiff){
                ut.setRoles(null);
            }
        }
        if(ub!=null && ut.getPermissions() != null ){
            Boolean isDiff = false;
            if(ub.getPermissions().size()==ut.getPermissions().size()){
                for(Permissions permissions:ub.getPermissions()){
                    if(!ub.getPermissions().contains(permissions)){
                        isDiff = true;
                        break;
                    }
                }
            }
            if(!isDiff){
                ut.setPermissions(null);
            }
        }
        return ut;
    }
}
