package com.xymall.baby.service.impl;

import com.xymall.baby.Utils.MD5Util;
import com.xymall.baby.dao.AdminUserMapper;
import com.xymall.baby.entity.AdminUser;
import com.xymall.baby.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password) {
        //TODO 这里为了测试方便，在将存入数据库中的用户密码的时候没有使用MD5加密，项目上线可以加
//        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return adminUserMapper.login(userName, password);
    }

    @Override
    public AdminUser getAdminUserDetailById(int userId) {
        return adminUserMapper.getAdminUserDetailById(userId);
    }
    public boolean updatePassword(int userId,String originalPassword,String newPassword){
        AdminUser adminUser = adminUserMapper.getAdminUserDetailById(userId);

        if(adminUser!=null){
//            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
//            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            String originalPasswordMd5 = originalPassword;
            String newPasswordMd5 = newPassword;
            if(originalPasswordMd5.equals(adminUser.getPassword())){
                adminUser.setPassword(newPasswordMd5);
                if(adminUserMapper.updateByAdminUser(adminUser)>0){
                    return true;
                }
            }
        }
        return false;
    }
}
