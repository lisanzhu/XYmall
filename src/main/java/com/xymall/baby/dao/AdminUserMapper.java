package com.xymall.baby.dao;

import com.xymall.baby.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminUserMapper {
    
    AdminUser login(@Param("userName") String userName, @Param("password") String password );
    AdminUser getAdminUserDetailById(@Param("userId") int userId);
    int updateByAdminUser(@Param("adminUser") AdminUser adminUser);
}
