package com.testmanage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
public class Roles implements Serializable {

    private Long id;
    private String roleName;
    private Set<Permissions> permissions;
}
