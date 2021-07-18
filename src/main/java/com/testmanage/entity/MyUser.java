package com.testmanage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class MyUser implements Serializable {

    private int id;
    private String username;
    private String password;
    private Set<Roles> roles;
    private Set<Permissions> Permissions;
}
