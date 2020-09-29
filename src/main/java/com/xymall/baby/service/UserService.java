package com.xymall.baby.service;

import com.xymall.baby.entity.User;

public interface UserService {
    User login(String userName, String password);
}
