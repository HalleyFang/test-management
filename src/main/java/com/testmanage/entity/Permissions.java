package com.testmanage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Permissions implements Serializable {

    private Long id;
    private String permissionsName;
}
