package com.testmanage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUser implements Serializable {

    public MyUser(String username,String isV){
        this.username = username;
        this.isV = isV;
    }

    private int id;
    private String username;
    private String password;
    private Set<Roles> roles;
    private Set<Permissions> Permissions;
    private String isV;
    private String salt;
    private Integer state = 0;//0正常 1锁定
}
