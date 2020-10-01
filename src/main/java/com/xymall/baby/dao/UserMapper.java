package com.xymall.baby.dao;


import com.xymall.baby.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface UserMapper {
    User login(@RequestParam("userName") String userName, @RequestParam("password") String password );
}
