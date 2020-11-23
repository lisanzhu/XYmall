package com.xymall.baby.service;

import com.xymall.baby.Utils.PageQueryUtil;
import com.xymall.baby.Utils.PageResult;
import com.xymall.baby.entity.User;
import com.xymall.baby.vo.UserVO;

import javax.servlet.http.HttpSession;

public interface UserService {
    String login(String userName, String password, HttpSession httpSession);
    PageResult getUserPage(PageQueryUtil pageQueryUtil);
    String register(String userName,String password);
    boolean lockUser(Integer[] ids, int lockStatus);
    UserVO updateUserInfo(User user, HttpSession httpSession);
}
