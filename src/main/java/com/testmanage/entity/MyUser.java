package com.testmanage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class MyUser implements Serializable {

    public MyUser(){
    }

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
}
