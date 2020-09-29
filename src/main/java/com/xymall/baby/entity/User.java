package com.xymall.baby.entity;

import lombok.Data;

@Data
public class User {
    private String userName;
    private String password;
    private String email;
    private boolean active;
    private long userId;
}
