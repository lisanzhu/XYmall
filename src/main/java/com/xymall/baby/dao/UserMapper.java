package com.xymall.baby.dao;


import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface UserMapper {
    User login(@Param("userName") String userName, @Param("password") String password );
    void register(@Param("userName") String userName,
                  @Param("email") String email, @Param("password") String password);
    List<User> findUserList(PageQueryUtil pageQueryUtil);
    int countUserNum();
    int lockUser(Integer[] ids,int lockStatus);
    User selectByUserName(String userName);
    int insertSelective(User user);
    User queryById(Long userId);
    int updateByPrimaryKeySelective(User user);
    User selectByPrimaryKey(Long id);
}
