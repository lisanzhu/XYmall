package com.xymall.baby.service.impl;
import com.xymall.baby.dao.UserMapper;
import com.xymall.baby.entity.User;
import com.xymall.baby.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public User login(String userName,String password){
        return userMapper.login(userName, password);
    }
}
