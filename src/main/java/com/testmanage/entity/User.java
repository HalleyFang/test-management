package com.testmanage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
}
