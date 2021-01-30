package com.xymall.baby.service;

import com.xymall.baby.entity.AdminUser;

public interface AdminUserService {
    AdminUser login(String username,String password);
    AdminUser getAdminUserDetailById(int userId);
    boolean updatePassword(int userId,String originalPassword,String newPassword);
}
