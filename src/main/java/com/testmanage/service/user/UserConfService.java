package com.testmanage.service.user;

import com.testmanage.mapper.UserConfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserConfService {

    @Autowired
    UserConfMapper userConfMapper;

    @CachePut("isV")
    public void setV(String username, String value) {
        userConfMapper.setParameter(username, "isV", value);
    }


    @Cacheable("isV")
    public String getV(String username) {
        return userConfMapper.getParameter(username, "isV");
    }
}
